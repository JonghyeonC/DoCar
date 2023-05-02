package com.pororo.docar.domain.checkoutBook.service;

import com.pororo.docar.common.exception.ResourceNotFoundException;
import com.pororo.docar.domain.book.entity.Book;
import com.pororo.docar.domain.book.repository.BookRepository;
import com.pororo.docar.domain.checkoutBook.dto.borrowBookDto;
import com.pororo.docar.domain.checkoutBook.entity.CheckoutBook;
import com.pororo.docar.domain.checkoutBook.repository.CheckoutBookRepository;
import com.pororo.docar.domain.user.entity.User;
import com.pororo.docar.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CheckoutBookService {
    private final CheckoutBookRepository checkoutBookRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void borrowBook(borrowBookDto input) {
        List<User> userList = userRepository.findAll();
        List<Book> bookList = bookRepository.findAll();

        for (User user : userList) {
            if (input.getUserId() == user.getId()) {
                for (Book book : bookList) {
                    if (input.getBookId() == book.getId()) {
                        CheckoutBook checkoutBook = CheckoutBook.builder()
                                .book(bookRepository.findById(input.getBookId()).orElseThrow(() -> new ResourceNotFoundException("책을 확인해주세요.")))
                                .user(userRepository.findById(input.getUserId()).orElseThrow(() -> new ResourceNotFoundException("유저를 확인해주세요.")))
                                .build();
                        checkoutBookRepository.save(checkoutBook);
                    }
                }
            }
        }
    }
}
