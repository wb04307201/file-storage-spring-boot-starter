package cn.wubo.file.storage.core;

import cn.wubo.file.storage.exception.FileStorageRuntimeException;
import cn.wubo.file.storage.file_name_mapping.IFileNameMapping;
import cn.wubo.file.storage.platform.IFileStorage;
import cn.wubo.file.storage.record.IFileStroageRecord;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class FileStorageService implements DisposableBean {

    CopyOnWriteArrayList<IFileStorage> fileStorageList;

    IFileStroageRecord fileStroageRecord;

    IFileNameMapping fileNameMapping;

    public FileStorageService(List<IFileStorage> fileStorageList, IFileStroageRecord fileStroageRecord, IFileNameMapping fileNameMapping) {
        this.fileStorageList = new CopyOnWriteArrayList<>(fileStorageList);
        this.fileStroageRecord = fileStroageRecord;
        this.fileNameMapping = fileNameMapping;
    }

    /**
     * 根据别名获取文件存储实例。
     * 该方法通过遍历文件存储列表，找到支持指定别名的文件存储实例。如果找到，则返回该实例；
     * 如果未找到，抛出FileStorageRuntimeException异常，表示未找到对应的平台配置。
     *
     * @param alias 文件存储的别名，用于标识不同的文件存储平台。
     * @return 支持指定别名的文件存储实例。
     * @throws FileStorageRuntimeException 如果未找到支持指定别名的文件存储实例。
     */
    private IFileStorage getFileStorage(String alias) {
        // 使用流式编程，过滤出支持指定别名的文件存储实例
        Optional<IFileStorage> fileStorageOptional = fileStorageList.stream().filter(fileStorage -> fileStorage.supportAlias(alias)).findAny();

        // 检查是否找到支持指定别名的文件存储实例
        if (fileStorageOptional.isPresent()) {
            IFileStorage fileStorage = fileStorageOptional.get();
            // 打印日志，记录找到的文件存储实例的别名
            log.debug("找到存储 {} 成功", fileStorage.getPlatformAlias());
            return fileStorage;
        } else {
            // 如果未找到，抛出异常
            throw new FileStorageRuntimeException(String.format("未找到别名【%s】对应的平台配置", alias));
        }
    }

    /**
     * 保存上传的文件并记录相关信息。
     * <p>
     * 本方法接收一个封装了文件信息的对象，通过对文件名进行映射，调用文件存储服务保存文件，
     * 最后记录文件存储信息到数据库中。
     *
     * @param fileWrapper 包含上传文件信息的封装对象，包括原始文件名和文件别名等。
     * @return FileInfo 文件存储后的信息，包括文件路径、文件名等。
     */
    @Transactional(rollbackOn = Exception.class)
    public FileInfo save(MultipartFileStorage fileWrapper) {
        // 对原始文件名进行映射，以得到新的文件名
        fileWrapper.setName(fileNameMapping.mapping(fileWrapper.getOriginalFilename()));

        // 调用文件存储服务保存文件，并返回文件信息
        FileInfo fileInfo = getFileStorage(fileWrapper.getAlias()).save(fileWrapper);

        // 将文件存储信息记录到数据库中，返回存储后的文件信息
        return fileStroageRecord.save(fileInfo);
    }

    /**
     * 根据FileInfo对象列出文件信息。
     * <p>
     * 本方法通过调用fileStroageRecord对象的list方法，传入FileInfo对象来获取并返回相应的文件信息列表。
     * 主要用于在文件存储系统中查询文件的元数据或属性信息。
     *
     * @param fileInfo FileInfo对象，包含用于查询文件信息的条件。
     * @return 返回一个FileInfo对象的列表，这些对象符合查询条件。
     */
    public List<FileInfo> list(FileInfo fileInfo) {
        return fileStroageRecord.list(fileInfo);
    }

    /**
     * 根据文件ID删除文件及其存储记录。
     * <p>
     * 此方法首先尝试根据提供的ID从文件存储记录中查找文件信息。如果找到文件信息，
     * 则尝试通过文件的别名从实际的文件存储系统中删除该文件。如果文件删除成功，
     * 接着尝试从文件存储记录中删除对应的文件记录。如果这两步操作都成功，则返回true，
     * 表示文件及其存储记录都被成功删除；否则返回false。
     *
     * @param id 文件的唯一标识符，用于查找和删除文件及其存储记录。
     * @return 如果文件和存储记录都被成功删除，则返回true；否则返回false。
     */
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(String id) {
        // 根据ID查找文件信息
        FileInfo fileInfo = fileStroageRecord.findById(id);
        // 通过文件别名从文件存储系统中删除文件，并尝试从文件存储记录中删除文件记录
        return getFileStorage(fileInfo.getAlias()).delete(fileInfo) && fileStroageRecord.delete(fileInfo);
    }

    /**
     * 检查文件是否存在于存储系统中。
     * <p>
     * 通过文件的唯一标识符ID，首先从文件存储记录中查找文件信息。然后，使用文件的别名从实际的文件存储系统中检索文件存储信息。
     * 最终，通过文件存储系统确认文件是否存在。
     *
     * @param id 文件的唯一标识符，用于查找文件信息。
     * @return 如果文件存在，则返回true；否则返回false。
     */
    public Boolean exists(String id) {
        // 从文件存储记录中查找文件信息
        FileInfo fileInfo = fileStroageRecord.findById(id);
        // 使用文件的别名查询文件存储系统，检查文件是否存在
        return getFileStorage(fileInfo.getAlias()).exists(fileInfo);
    }

    /**
     * 根据文件ID下载文件。
     * <p>
     * 本方法通过文件ID从文件存储记录中查找对应的文件信息，然后调用相应的文件存储服务下载文件。
     * 这里的文件存储可能是分布式的，不同的文件可能存储在不同的位置，通过文件别名来定位文件。
     *
     * @param id 文件存储记录的唯一标识符。这个ID用于在文件存储记录中查找对应的文件信息。
     * @return 返回一个MultipartFileStorage对象，包含了文件下载的相关信息和操作。
     */
    public MultipartFileStorage download(String id) {
        // 根据ID查找文件信息
        FileInfo fileInfo = fileStroageRecord.findById(id);
        // 根据文件别名获取文件存储服务，并使用该服务下载文件
        return getFileStorage(fileInfo.getAlias()).download(fileInfo);
    }

    /**
     * 销毁文件存储实例。
     * 此方法遍历文件存储列表，尝试关闭每个文件存储实例，并记录相关日志。
     * 如果关闭操作失败，将捕获并记录异常。
     *
     * @throws Exception 如果关闭任何一个文件存储实例时发生异常，则抛出此异常。
     */
    @Override
    public void destroy() throws Exception {
        // 遍历文件存储列表
        for (IFileStorage fileStorage : fileStorageList) {
            try {
                // 尝试关闭文件存储实例
                fileStorage.close();
                // 记录成功关闭的日志
                log.debug("销毁存储 {} 成功", fileStorage.getPlatformAlias());
            } catch (Exception e) {
                // 捕获并记录关闭操作中的异常
                log.error("销毁存储 {} 失败，{}", fileStorage.getPlatformAlias(), e.getMessage(), e);
            }
        }
    }

}
