package com.application.bookstore.service;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.AuthorRequestDto;
import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.BookRequestDto;
import com.application.bookstore.model.Author;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    //create author
    public AuthorDto createAuthor(AuthorDto authorDto){

        Author author=new Author();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setEmail(authorDto.getEmail());
        author.setNationality(authorDto.getNationality());

        final Author saveAuthor = authorRepository.save(author);
        final AuthorDto result = toDto(saveAuthor);

        return result;

    }

    public AuthorDto createAuthorWithBooks(AuthorRequestDto authorDto){
        Author author=new Author();
        author.setId(authorDto.getId());
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setEmail(authorDto.getEmail());
        author.setNationality(authorDto.getNationality());


//        if(authorDto.getBookIds()==null|| authorDto.getBookIds().isEmpty()){
//            final Author save = authorRepository.save(author);
//            return toDto(save);
//        }
//with books
        List<Book> books=new ArrayList<>(bookRepository.findAllById(authorDto.getBookIds()));

        for (Book book:books){
            book.getAuthors().add(author);
        }
        author.setBooks(books);
        final Author save = authorRepository.save(author);
        return toDto(save);


    }

    //Get All Authors
    public List<AuthorDto> getAllAuthors(){
        final List<AuthorDto> result = authorRepository.findAll()
                .stream()
                .map(author -> toDto(author))
                .toList();
        return result;

    }
    //get Author by Id

    public AuthorDto getAuthorById(int id){
        final Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        return toDto(author);

    }

    //update
    public AuthorDto updateAuthor(int id,AuthorDto authorDto){
        Author author=authorRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Author not found"));
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setEmail(authorDto.getEmail());
        author.setNationality(authorDto.getNationality());

        final Author saveAuthor = authorRepository.save(author);
        final AuthorDto result = toDto(saveAuthor);
        return result;
    }

    //delete
    public void deleteAuthor(int id){
        authorRepository.deleteById(id);
    }




    //entity ->Dto
    private AuthorDto toDto(Author author){
        AuthorDto dto=new AuthorDto();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setEmail(author.getEmail());
        dto.setNationality(author.getNationality());

        final List<BookRequestDto> dtos = author.getBooks().stream()
                .map(book -> {
                    BookRequestDto bookRequestDto = new BookRequestDto();
                    bookRequestDto.setId(book.getId());
                    bookRequestDto.setTitle(book.getTitle());
                    return bookRequestDto;
                }).toList();
        dto.setBookIds(dtos);

        return dto;
    }
}