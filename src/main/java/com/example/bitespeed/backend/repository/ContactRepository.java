package com.example.bitespeed.backend.repository;

import com.example.bitespeed.backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    @Query(value = "select * from contact where id = ?1", nativeQuery = true)
    List<Contact> getContactsById(Integer id);

    @Query(value = "select * from contact where email = ?1", nativeQuery = true)
    List<Contact> getContactsByEmail(String email);

    @Query(value = "select * from contact where phone_number = ?1", nativeQuery = true)
    List<Contact> getContactsByNumber(String phoneNumber);

    @Query(value = "select * from contact where email = ?1 or phone_number = ?2", nativeQuery = true)
    List<Contact> getContactsByEmailOrNumber(String email, String phoneNumber);

    @Query(value = "select * from contact where email = ?1 and phone_number = ?2", nativeQuery = true)
    List<Contact> getContactsByEmailAndNumber(String email, String phoneNumber);

    @Query(value = "select * from contact where linked_id = ?1 or id = ?1 or linked_id = ?2 or id = ?2 or email = ?3 or phone_number = ?4", nativeQuery = true)
    List<Contact> getAllLinkedContacts(Integer id, Integer linkedId, String email, String phoneNumber);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update contact set linked_id= ?2 , link_precedence = ?3 where id = ?1", nativeQuery = true)
    Object updateContact(Integer id, Integer linkedId, String linkPrecedence);


}
