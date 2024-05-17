package com.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.board.domain.image.PostImage;
import com.board.exception.image.ImageOverLength;
import com.board.exception.image.ImageOverSize;
import com.board.exception.image.InvalidUploadImage;
import com.board.repository.image.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3service  {
    private final AmazonS3 amazonS3;
    private final PostImageRepository postImageRepository;

    //버킷 이름
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public List<String> saveFile(List<MultipartFile> multipartFiles,String dirName) throws IOException {
        //사진 갯수는 5개 제한
        if(multipartFiles.size()>5){
            throw new ImageOverLength();
        }

        //파일이 유효한지 사전 검사
        for(MultipartFile multipartFile : multipartFiles) {
            ///image 파일인지 확인
            if (!Objects.requireNonNull(multipartFile.getContentType()).startsWith("image")) {
                throw new InvalidUploadImage();
            }
            //이미지가 5MB 넘어가면 Exception
            if (multipartFile.getSize() > 5000000) {
                throw new ImageOverSize();
            }
        }

        List<String> listUrl = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles) {
            //파일 확장자 분리 및 파일 명 중복방지로 랜덤으로 생성
            String saveFileName=getFileName(multipartFile,dirName);

            //multipartFile의 size와 contentType을 명시
            ObjectMetadata metadata =new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            //s3에 파일 업로드
            amazonS3.putObject(bucket,saveFileName,multipartFile.getInputStream(),metadata);
            listUrl.add(amazonS3.getUrl(bucket,saveFileName).toString());
        }

        return listUrl;
    }
    //파일 확장자 분리 및 파일 명 중복방지로 랜덤으로 생성
    public String getFileName(MultipartFile multipartFile,String dirName){
        String extension = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return dirName+"/"+uuid + extension;
    }

    public void deleteFiles(List<String> deleteFiles,String dirName) throws IOException {
        for(String url:deleteFiles) {

            //파일 이름 넘겨주면 s3에서 삭제와 DB에서 삭제 두개 다 진행할 것 입니다.
            deleteFile(url, dirName);

        }

    }

    public void deleteFile(String imgFileName,String dirName){
        // URL에서 폴더이름/파일 이름을 Key값으로 넣어줘야함

        String[] parts = imgFileName.split("/");
        String objectKey = dirName+"/"+parts[parts.length - 1];
        amazonS3.deleteObject(new DeleteObjectRequest(bucket,objectKey));

        PostImage postImage=postImageRepository.findByName(imgFileName);
        postImageRepository.delete(postImage);
    }


}
