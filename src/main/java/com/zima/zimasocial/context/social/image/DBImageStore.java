package com.zima.zimasocial.context.social.image;

import com.zima.zimasocial.entity.file.FileCode;
import com.zima.zimasocial.entity.file.FileEntity;
import com.zima.zimasocial.entity.file.FileType;
import com.zima.zimasocial.repository.FileEntityRepository;
import com.zima.zimasocial.context.social.image.exception.FileNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DBImageStore implements ImageService {
    private final FileEntityRepository fileEntityRepository;

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUserId(CurrentUser.getCurrentUserProfile().getId());
        fileEntity.setName(generateFileName(file));
        fileEntity.setData(file.getBytes());
        fileEntity.setMediaType(Arrays.stream(Objects.requireNonNull(file.getOriginalFilename()).split("\\.")).toList().getLast());
        fileEntity.setFileType(FileType.PROFILE_IMAGE);
        fileEntity.setSize(file.getSize());

        fileEntityRepository.save(fileEntity);
        return fileEntity.getName();
    }

    @Override
    public String uploadFile(MultipartFile file) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(generateFileName(file));
        fileEntity.setData(fileEntity.getData());
        fileEntity.setMediaType(fileEntity.getMediaType());
        fileEntity.setCode(FileCode.appIcon);
        fileEntity.setSize(file.getSize());

        fileEntityRepository.save(fileEntity);
        return fileEntity.getName();
    }
    @Override
    public void deleteFile(String key) {
        Optional<FileEntity> file = fileEntityRepository.findByName(key);
        file.ifPresent(fileEntityRepository::delete);
    }

    @Override
    public byte[] getFile(String fileName) {
        Optional<FileEntity> file = fileEntityRepository.findByName(fileName);
        if(file.isEmpty()) throw new FileNotFoundException();
        return file.get().getData();
    }
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "_");
    }
}
