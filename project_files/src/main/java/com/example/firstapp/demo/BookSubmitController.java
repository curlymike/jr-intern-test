package com.example.firstapp.demo;

import com.example.firstapp.demo.model.Book;
import com.example.firstapp.demo.repository.BooksRepository;
import com.example.firstapp.demo.validator.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class BookSubmitController {
    @Autowired
    BooksRepository booksRepository;

    @Autowired
    private BookValidator bookValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(bookValidator);
    }

    @PostMapping("/book/add")
    public String bookSave(@RequestParam(value = "url", required=false) String url, @Valid Book book, BindingResult result, Model model, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {
        System.out.println("BookSubmitController:book/add");

        System.out.println("addBook: Id: " + book.getId());
        System.out.println("addBook: Title: " + book.getTitle());
        System.out.println("addBook: Description: " + book.getDescription());
        System.out.println("addBook: Author: " + book.getAuthor());
        System.out.println("addBook: Isbn: " + book.getIsbn());
        System.out.println("addBook: printYear: " + book.getPrintYear());
        System.out.println("addBook: readAlready: " + book.getReadAlready());

        if (result.hasErrors()) {

            model.addAttribute("remoteUrl", url);

            for (ObjectError err : result.getAllErrors()) {
                System.out.println(err.toString());
            }

            //System.out.println("result.hasErrors(): " + result.hasErrors());

            return "book";
        }

        //---

        Book newBook = new Book();

        //newBook.setId(0L);
        newBook.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());
        newBook.setAuthor(book.getAuthor());
        newBook.setIsbn(book.getIsbn());
        newBook.setPrintYear(book.getPrintYear());
        newBook.setReadAlready(book.getReadAlready());

        booksRepository.save(newBook);

        if (newBook.getId() != null) {
            System.out.println("+++ Success! new book id: " + newBook.getId());
            redirectAttributes.addFlashAttribute("STATUS_MESSAGE", "Книга успешно добавлена.");
            //return "redirect:/book/" + newBook.getId();
        }

        return "redirect:/";
    }

    @PostMapping("/book/{id}")
    public String bookSave(@Valid Book book, BindingResult result, @PathVariable("id") long id, @RequestParam(value = "page", required=false) Integer pageNum, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {

        System.out.println("BookSubmitController:book/" + id + "; pageNum: " + pageNum);

        System.out.println("saveBook: Id: " + book.getId());
        System.out.println("saveBook: Title: " + book.getTitle());
        System.out.println("saveBook: Description: " + book.getDescription());
        System.out.println("saveBook: Author: " + book.getAuthor());
        System.out.println("saveBook: Isbn: " + book.getIsbn());
        System.out.println("saveBook: printYear: " + book.getPrintYear());
        System.out.println("addBook: readAlready: " + book.getReadAlready());

        if (result.hasErrors()) {
            for (ObjectError err : result.getAllErrors()) {
                System.out.println(err.toString());
            }
            //Map<String, Object> model = new HashMap<>();
            System.out.println("result.hasErrors(): " + result.hasErrors());
            //model.put("errors", "There are errors.");
            //model.put("book", form);
            //return new ModelAndView("book", model);
            return "book";
        }

        Book existingBook = booksRepository.findOne(book.getId());

        boolean resetReadAlready = false;

        if (book != null) {

            // Поле printYear может быть null
            Integer bookYear = book.getPrintYear();
            Integer existingYear = existingBook.getPrintYear();

            resetReadAlready = !(existingBook.getTitle().equals(book.getTitle()) &&
            existingBook.getDescription().equals(book.getDescription()) &&
            existingBook.getIsbn().equals(book.getIsbn()) &&
                (bookYear == null ? (bookYear == existingYear) : bookYear.equals(existingYear)));


            existingBook.setTitle(book.getTitle());
            existingBook.setDescription(book.getDescription());
            // Author field should be permanent
            //existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setPrintYear(book.getPrintYear());
            if (resetReadAlready) {
                existingBook.setReadAlready(false);
            } else {
                existingBook.setReadAlready(book.getReadAlready());
            }
            booksRepository.save(existingBook);

            String msg = "<div>Информация сохранена.</div>";

            if (resetReadAlready && book.getReadAlready() == true) {
                msg += "<div>Статус книги сброшен на &quot;не прочитана&quot; (такое бизнес-требование).</div>";
            }

            redirectAttributes.addFlashAttribute("STATUS_MESSAGE", msg);
        }

        return "redirect:/" + (pageNum == null ? "" : "?page=" + pageNum);
    }

    @PostMapping("/book/{id}/{action}")
    public String bookSave(@RequestParam String op, @PathVariable("id") long id, @PathVariable("action") String action, @RequestParam(value = "page", required=false) Integer pageNum, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {

        //System.out.println("BookSubmitController:book/" + id + "/" + action + "; op: " + op);

        if (op.equals("Delete") || op.equals("Удалить")) {
            Book book = booksRepository.findOne(id);
            if (book == null) {
                redirectAttributes.addFlashAttribute("STATUS_MESSAGE", "Книга с id " + id + " не существет.");
            } else {
                booksRepository.delete(id);
                redirectAttributes.addFlashAttribute("STATUS_MESSAGE", "Книга &quot;<em>" + book.getTitle() + "</em>&quot; (id <em>" + id + "</em>) безвозвратно удалена.");
            }

            Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "id"));
            Page<Book> page = booksRepository.findAll(pageable);

            System.out.println("Debuggin: pageNum: " + pageNum);
            System.out.println("Debuggin: getTotalPages: " + page.getTotalPages());

            // Если удалить последнюю книгу на последней странице
            // то количество страниц уменьшится на единицу и редирект
            // на эту страницу обернётся ошибкой 404.

            int lastPage = page.getTotalPages() - 1;

            if (pageNum != null && pageNum > lastPage) {
                pageNum = lastPage;
            }

            return "redirect:/" + (pageNum == null ? "" : "?page=" + pageNum);
        }

        if (op.equals("Cancel") || op.equals("Отмена")) {
            redirectAttributes.addFlashAttribute("STATUS_MESSAGE", "Ну нет так нет.");
        }

        return "redirect:/" + (pageNum == null ? "" : "?page=" + pageNum);
    }

}
