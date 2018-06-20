package com.example.firstapp.demo.model;

import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)

public class Book implements Serializable {

    //  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    //  `title` VARCHAR(100) NOT NULL DEFAULT '',
    //  `description` VARCHAR(255) NOT NULL DEFAULT '',
    //  `author` VARCHAR(100) NOT NULL DEFAULT '',
    //  `isbn` VARCHAR(20) NOT NULL DEFAULT '',
    //  `printYear` INT UNSIGNED NOT NULL DEFAULT '0',
    //  `createdAt` INT UNSIGNED NOT NULL DEFAULT '0',
    //  `updatedAt` INT UNSIGNED NOT NULL DEFAULT '0',
    //  `readAlready` INT UNSIGNED NOT NULL DEFAULT '0',

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(length = 100)
    @Size(max=100)
    private String title;

    @Lob
    private String description = "";

    @NotBlank
    @Column(length = 100)
    @Size(max=100)
    private String author;

    @Column(length = 20)
    @Size(min=10, max=13)
    private String isbn;

    private Integer printYear;

    private Boolean readAlready;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    //---

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    //---

    public Book() {

    }

    public Book(String title, String description, String author, String isbn, Integer printYear, Boolean readAlready) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.isbn = isbn;
        this.printYear = printYear;
        this.readAlready = readAlready;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        System.out.println("setId call");
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Column(name = "printYear")
    public Integer getPrintYear() {
        return printYear;
    }

    public void setPrintYear(Integer printYear) {
        this.printYear = printYear;
    }

    @Column(name = "readAlready")
    public Boolean getReadAlready() {
        return readAlready;
    }

    public void setReadAlready(Boolean readAlready) {
        this.readAlready = readAlready;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
