package com.arism.mapper;

import com.arism.dto.CommentDto;
import com.arism.dto.ProductDto;
import com.arism.model.Comment;
import com.arism.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "image", source = "image")
    ProductDto toDto(Product product);

    @Mapping(target = "image", source = "image")
    Product toEntity(ProductDto productDto);


    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);
}
