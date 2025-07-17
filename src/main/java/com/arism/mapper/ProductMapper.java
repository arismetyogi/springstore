package com.arism.mapper;

import com.arism.dto.CommentDto;
import com.arism.dto.ProductDto;
import com.arism.model.Comment;
import com.arism.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);

    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);
}
