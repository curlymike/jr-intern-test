package com.example.firstapp.demo;

import com.example.firstapp.demo.model.Book;
import com.example.firstapp.demo.model.Confirmation;
import com.example.firstapp.demo.repository.BooksRepository;
import com.example.firstapp.demo.stuff.Litres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    BooksRepository booksRepository;

//    @GetMapping("/")
//    public String indexWithPage(Map<String, Object> model) throws ResourceNotFoundException {
//        return index(0, model);
//    }

    //@GetMapping(value = "/", params = {"page"})
//    @GetMapping(value = "/")
//    public String indexWithPage(
//        @RequestParam(value = "page", required=false) Integer pageNum,
//        @RequestParam(value = "searchText", required=false) String searchText,
//        Map<String, Object> model) throws ResourceNotFoundException {
//        return index(pageNum, model);
//    }

    @GetMapping(value = "/")
    public String index(@RequestParam(value = "page", required=false) Integer pageNum,
                        @RequestParam(value = "searchText", required=false) String searchText,
                        @RequestParam(value = "searchScope", required=false) String searchScope,
                        Map<String, Object> model) throws ResourceNotFoundException {
        //System.out.println("IndexController:index");

        if (pageNum == null) {
            pageNum = 0;
        }

        System.out.println("IndexController:index; page: " + pageNum);

        model.put("time", new Date());
        model.put("message", "Hello!");

        //List<Book> books = booksRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        //List<Book> books = booksRepository.findByTitleContaining("test");

        Pageable pageable = new PageRequest(pageNum, 10, new Sort(Sort.Direction.DESC, "id"));
        List<Book> books;
        Page<Book> page = null;
        if (searchText == null || searchText.isEmpty()) {
            page = booksRepository.findAll(pageable);
            books = page.getContent();
        } else {
            // Отбрасываем пробелы в начале и конце строки
            searchText = searchText.trim();

            //books = booksRepository.findByTitleContaining(searchText);
            if (searchScope == null || searchScope.equals("title")) {
                books = booksRepository.findByTitleContaining(searchText);
                model.put("searchScopeTitle", true);
            } else if (searchScope.equals("description")) {
                books = booksRepository.findByDescriptionContaining(searchText);
                model.put("searchScopeDescription", true);
            } else {
                books = booksRepository.findByTitleContainingOrDescriptionContaining(searchText, searchText);
                model.put("searchScopeBoth", true);
            }
            //books = booksRepository.findByDescriptionContaining(searchText);
            model.put("isSearch", true);
        }

        if (page != null && pageNum > 0 && page.getNumber() >= page.getTotalPages()) {
            throw new ResourceNotFoundException("Страница не существует.");
        }

        Utility.chopDescription(books, 80);

        model.put("books", books);
        model.put("showPager", (page != null));
        model.put("pageNum", pageNum);
        model.put("elementsCount", books.size());
        model.put("totalPages", (page == null ? 0 : page.getTotalPages()));
        model.put("totalElements", (page == null ? 0 : page.getTotalElements()));

        model.put("searchText", searchText);

        return "index";
    }

    @GetMapping("/book/add")
    public String bookEdit(
        @RequestParam(value = "page", required=false) Integer pageNum,
        @RequestParam(value = "url", required=false) String url,
        Model model) {

        Book book = new Book();

        if (url != null && url.length() > 0) {
            try {
                URL bookUrl = new URL(url);
                Litres litres = new Litres(bookUrl);

                System.out.println("URL: " + bookUrl + " " + litres.status() + " " + litres.getContentLength());

                //System.out.println("---------------------");
                //System.out.println(litres);
                //System.out.println("---------------------");

                book.setTitle(litres.getTitle());
                book.setDescription(litres.getDescription());
                book.setAuthor(litres.getAuthor());
                book.setIsbn(litres.getIsbn());
                book.setPrintYear(litres.getYear());

            } catch (MalformedURLException e) {

            }
        }

        model.addAttribute("book", book);
        //model.addAttribute("remoteUrl", new RemoteUrl(url));
        model.addAttribute("remoteUrl", url);
        return "book";
    }

    @GetMapping("/book/{id}")
    public String bookEdit(@PathVariable("id") long id, Model model) throws ResourceNotFoundException {
        //Map<String, Object> model = new HashMap<>();
        model.addAttribute("message", "Hello!");

        //Neither BindingResult nor plain target object for bean name 'book' available as request attribute

        // На будущее: лучше использовать
        // Book book1 = booksRepository.findOne(id);
        // если книги с таким id нет - book1 будет null
        if (booksRepository.exists(id)) {
            Book book = booksRepository.getOne(id);
            //model.put("bookId", id);
            //model.put("book", book);
            model.addAttribute("bookId", id);
            model.addAttribute("book", book);
        } else {
            throw new ResourceNotFoundException("Нет книги с таки Id.");
        }

        return "book";
    }

    @GetMapping("/book/{id}/{action}")
    public String bookEdit(@PathVariable("id") long id, @PathVariable("action") String action, Model model, @RequestParam(value = "page", required=false) Integer pageNum, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {

        Book book = booksRepository.findOne(id);
        if (book == null) {
            throw new ResourceNotFoundException("Нет книги с таки Id.");
        }

        if (action.equals("delete")) {
            model.addAttribute("message", "<strong>" + book.getTitle() + "</strong>");
            Confirmation conf = new Confirmation();
            model.addAttribute("confirmation", conf);
            return "deleteConfirm";
        }

        if (action.equals("toggle-read-already")) {
            //redirectAttributes.addFlashAttribute("STATUS_MESSAGE", "Work in progress...");
            book.setReadAlready(!book.getReadAlready());
            booksRepository.save(book);
            return "redirect:/" + (pageNum == null ? "" : "?page=" + pageNum);
        }

        throw new ResourceNotFoundException("Not Found");
    }

    @GetMapping("/test2")
    public ModelAndView test2() {
        Map<String, Object> model = new HashMap<>();
        //model.put("bookObject", booksRepository.getOne(55L));
        model.put("test", "This is a test string");
        return new ModelAndView("test2", model);
    }

    @GetMapping("/test3")
    public ModelAndView test3() {
        Map<String, Object> model = new HashMap<>();
        //model.put("bookObject", booksRepository.getOne(55L));
        String output = "";

//        output += "This is a test string";
//        Book book = new Book("Book 26", "This is a book 26", "Some Guy", "000000000026", 1992, false);
//        booksRepository.save(book);
//        output += " Id: " + book.getId();

        output += "This is for performing test stuff.";

        model.put("output", output);
        return new ModelAndView("test3", model);
    }


}