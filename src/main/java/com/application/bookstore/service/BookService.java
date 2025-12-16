package com.application.bookstore.service;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.BookRequestDto;
import com.application.bookstore.dto.BookWithNewAuthorDto;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Author;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BookDto getById(int id){


        final Optional<Book> optionalBook = bookRepository.findById(id);
        final BookDto result = optionalBook.map(book -> toDto(book))
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));

        return result;


    }
    public List<BookDto> getAll(){

        BookDto bookDto=new BookDto();
        Author author=new Author();
        final List<BookDto> result = bookRepository.findAll()
                .stream()
                .map(book -> toDto(book))
                .toList();

        return result;
    }

    //create book  without author
    public BookDto createBook(BookDto bookDto){

        Book book=new Book();
        book.setTitle(bookDto.getTitle());
        book.setGenre(bookDto.getGenre());
        book.setStock(bookDto.getStock());
        book.setPrice(bookDto.getPrice());

        final Book saveBook = bookRepository.save(book);
        return toDto(saveBook);


    }

    public BookDto createBookWithNewAuthor(BookWithNewAuthorDto bookDto) {
        Author author=new Author();
        author.setFirstName(bookDto.getAuthor().getFirstName());
        author.setLastName(bookDto.getAuthor().getLastName());
        author.setEmail(bookDto.getAuthor().getEmail());
        author.setNationality(bookDto.getAuthor().getNationality());
        author.setId(bookDto.getAuthor().getId());

        authorRepository.save(author);

        Book book=new Book();
        book.setTitle(bookDto.getBook().getTitle());
        book.setPrice(bookDto.getBook().getPrice());
        book.setStock(bookDto.getBook().getStock());
        book.setGenre(bookDto.getBook().getGenre());
        book.getAuthors().add(author);
        final Book saveBook = bookRepository.save(book);
        final BookDto result = toDto(saveBook);

        return result;

    }

    public BookDto attachAuthor(int bookId,int authorId){
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new RuntimeException("Book not found"));

        Author author=authorRepository.findById(authorId)
                .orElseThrow(()->new RuntimeException("Author not found"));

        book.getAuthors().add(author);
        final Book saveBook = bookRepository.save(book);
        return toDto(saveBook);

    }


    public void deleteOneById(int id){
        bookRepository.deleteById(id);


    }
    private BookDto toDto(Book book){

        BookDto result=new BookDto();
        result.setId(book.getId());
        result.setTitle(book.getTitle());
        result.setGenre(book.getGenre());
        result.setPrice(book.getPrice());
        result.setStock(book.getStock());
//        final List<AuthorDto> list = book.getAuthors().stream()
//                .map(author -> {
//                    AuthorDto authorDto = new AuthorDto();
//                    authorDto.setId(author.getId());
//                    authorDto.setFirstName(author.getFirstName());
//                    authorDto.setLastName(author.getLastName());
//                    return authorDto;
//                }).toList();
        List<AuthorDto> authorDtos=new ArrayList<>();
        if (book.getAuthors()!=null){
            for(Author author:book.getAuthors()){

                AuthorDto authorDto=new AuthorDto();
                authorDto.setId(author.getId());
                authorDto.setFirstName(author.getFirstName());
                authorDto.setLastName(author.getLastName());
                authorDto.setEmail(author.getEmail());
                authorDto.setNationality(author.getNationality());
                authorDtos.add(authorDto);
            }
        }
        result.setAuthor(authorDtos);
        return result;


    }


    //--------------------------------------------------------------
    //------------------- Validate BookRequestDto ------------------
    //--------------------------------------------------------------
    private void validateBookRequestDto(BookRequestDto bookRequestDto){
        if(bookRequestDto.getTitle()==null){
            throw new ValidationException("title");
        }
        if(bookRequestDto.getGenre()==null){
            throw new ValidationException("genre");
        }
        if (bookRequestDto.getPrice()==0){
            throw new ValidationException("price");
        }
        if (bookRequestDto.getPrice()<0){
            throw new ValidationException("stock","price should be greater than 0");
        }
//        if (bookRequestDto.getStock()==0){
//            throw new ValidationException("stock");
//        }

        if (bookRequestDto.getStock()<0){
            throw new ValidationException("stock","stock should be greater than 0");
        }
    }





}