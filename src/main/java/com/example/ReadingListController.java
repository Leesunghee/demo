package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by sunghee on 2016. 11. 7..
 */

@Controller
@RequestMapping("/")
@ConfigurationProperties(prefix="amazon") //프로퍼티 주입
public class ReadingListController {

    //private static final String reader = "craig";

    private ReadingListRepository readingListRepository;
    private String associatedId;

    @Autowired
    public ReadingListController(ReadingListRepository readingListRepository) {
        this.readingListRepository = readingListRepository;
    }

    public void setAssociateId(String associateId) { //제휴 ID의 세터 메서드
        this.associatedId = associateId;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String readersBooks(Reader reader, Model model) {
        List<Book> readingList = readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);

            model.addAttribute("amazonID", associatedId); //제휴 ID를 모델에 추가
        }
        System.out.println("#################### hot swapping test ok! ##");
        return "readingList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addToReadingList(Reader reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/";
    }



}
