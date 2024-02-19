package com.example.board.requestServiceDto.Post;

import com.example.board.requestDto.post.GetPostsDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetPostsServiceDto {
    private final GetPostsDto getPostsDto;
    private final static int SIZE=10;
    private String category;

    @Builder
    public GetPostsServiceDto(GetPostsDto getPostsDto,String category) {
        this.getPostsDto=getPostsDto;
        this.category = category;
    }

    public Long getOffset(){
        return (long) (Math.max(1,this.getPostsDto.getPage())-1)*SIZE;
    }

    public void defaultCategory(String category){
        this.category=category;
    }

    public int getSize(){
        return SIZE;
    }

}
