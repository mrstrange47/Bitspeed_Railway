package com.example.bitespeed.backend.service;

import com.example.bitespeed.backend.dto.IdentifyRequest;
import com.example.bitespeed.backend.dto.IdentifyResponse;
import com.example.bitespeed.backend.entity.Contact;
import com.example.bitespeed.backend.repository.ContactRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public IdentifyResponse getContacts() {
        return null;
    }

    public IdentifyResponse getContactsById(Long id) {
        //List<Contact> contacts = contactRepository.getContactsById(id);
        Optional<Contact> con = contactRepository.findById(id);
        Contact contact = con.get();
        IdentifyResponse response = new IdentifyResponse();
        List<String> emails = new ArrayList<>();
        List<String> contactNumbers = new ArrayList<>();
        List<Integer> secondaryContactIds = new ArrayList<>();

        if (contact.getLinkPrecedence().equalsIgnoreCase("primary")) {
                response.setPrimaryContactId(contact.getId());
            }
        else{
                secondaryContactIds.add(contact.getId());
            }
        emails.add(contact.getEmail());
        contactNumbers.add(contact.getPhoneNumber());

        response.setEmails(emails);
        response.setSecondaryContactIds(secondaryContactIds);
        response.setPhoneNumbers(contactNumbers);
        return response;
    }

    public IdentifyResponse getContactDetails(IdentifyRequest request){
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        List<Contact> ListOfContacts = new ArrayList<>();

        //::::::::::::::::::: Request handling:::::::::::::::::::::::::::::::::::::::


        //Case 1 => email null
        if(email == null){
            ListOfContacts = contactRepository.getContactsByNumber(phoneNumber);
        }

        //Case 2 => phone null
        else if(phoneNumber == null){
            ListOfContacts = contactRepository.getContactsByEmail(email);
        }

        //Case 3 => email and phone both exits
        else{
            List<Contact> temp1 = contactRepository.getContactsByEmail(email);
            List<Contact> temp2 = contactRepository.getContactsByNumber(phoneNumber);
            if(temp1.size()==0 || temp2.size()==0){
                Contact tempContact = createContact(email,phoneNumber);
            }

            ListOfContacts = contactRepository.getContactsByEmailOrNumber(email,phoneNumber);

        }


        List<Contact> ListOfLinkedContacts = new ArrayList<>();

        ListOfLinkedContacts = linkedContacts(ListOfContacts);

        //sort by created at oldest

        Collections.sort(ListOfLinkedContacts, Comparator.comparing(contact -> LocalDateTime.parse(contact.getCreatedAt(), dtf)));

        //set oldest as Primary and everyone else as secondary

        List<Contact> reOrderedContacts = getReorderContact(ListOfLinkedContacts);

        //get response

        IdentifyResponse response = getResponse(reOrderedContacts);

        return response;
    }

    List<Contact> linkedContacts(List<Contact> ListOfContacts){

        HashSet<Contact> setOfContacts = new HashSet();
        for(Contact contact : ListOfContacts){
            setOfContacts.add(contact);
            List<Contact> listLinkedContacts = contactRepository.getAllLinkedContacts(contact.getId(), contact.getLinkedId(), contact.getEmail(),contact.getPhoneNumber());
            for(Contact con : listLinkedContacts){
                setOfContacts.add(con);
            }
        }

        List<Contact> contacts = new ArrayList<>();

        Iterator<Contact> itrContact = setOfContacts.iterator();
        while(itrContact.hasNext()){
            contacts.add(itrContact.next());
        }

        return contacts;
    }

    List<Contact> getReorderContact(List<Contact> ListOfLinkedContacts){

        List<Contact> ordered = new ArrayList<>();

        Integer primaryId = null;
        int flag=0;
        for(Contact contact : ListOfLinkedContacts){
            if(flag == 0){
                primaryId = contact.getId();
                contactRepository.updateContact(contact.getId(),null,"primary");
                ordered.add(contactRepository.findById((long)(contact.getId())).get());
                flag=1;
                continue;
            }
            contactRepository.updateContact(contact.getId(),primaryId,"secondary");
            ordered.add(contactRepository.findById((long)(contact.getId())).get());
        }
        return ordered;
    }

    IdentifyResponse getResponse(List<Contact> ListOfcontacts){

        Integer primaryContactId = null;

        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<Integer> secondaryContactIds = new ArrayList<>();

        HashSet<String> setOfEmails=new HashSet();
        HashSet<String> setOfPhoneNumbers=new HashSet();
        HashSet<Integer> setOfSecondaryContactIds=new HashSet();


        int flag=0;
        for(Contact contact : ListOfcontacts){
            if(contact.getLinkPrecedence().equalsIgnoreCase("primary")){
                primaryContactId = contact.getId();
            }
            else{
                setOfSecondaryContactIds.add(contact.getId());
            }
            setOfEmails.add(contact.getEmail());
            setOfPhoneNumbers.add(contact.getPhoneNumber());
        }

        Iterator<String> itrEmail=setOfEmails.iterator();
        while(itrEmail.hasNext()){
            emails.add(itrEmail.next());
        }
        Iterator<String> itrPhone=setOfPhoneNumbers.iterator();
        while(itrPhone.hasNext()){
            phoneNumbers.add(itrPhone.next());
        }
        Iterator<Integer> itrSecondaryId=setOfSecondaryContactIds.iterator();
        while(itrSecondaryId.hasNext()){
            secondaryContactIds.add(itrSecondaryId.next());
        }

        return new IdentifyResponse(primaryContactId,emails,phoneNumbers ,secondaryContactIds);
    }


    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    private Contact createContact(String email, String phoneNumber) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateFormat = now.format(dtf);
        Contact contact = new Contact(phoneNumber,email,null,"primary",dateFormat,dateFormat,null);

        addContact(contact);
        return contact;
    }
}
