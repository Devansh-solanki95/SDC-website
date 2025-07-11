package com.sdc.services;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc.entity.Faq;
import com.sdc.models.FaqModel;
import com.sdc.repo.FaqRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;

    public Faq addFaq(FaqModel faqModel) {
        Faq faq = new Faq();
        faq.setQues(faqModel.getQues());
        faq.setAns(faqModel.getAns());
        return faqRepository.save(faq);
    }

    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    public boolean deleteFaq( Integer id) {
        faqRepository.deleteById(id);
		return false;
    }

    public Faq updateFaq( Integer id, FaqModel faqModel) {
        Faq faq = faqRepository.findById(id).orElse(null);
        if (faq != null) {
            faq.setQues(faqModel.getQues());
            faq.setAns(faqModel.getAns());
            return faqRepository.save(faq);
        }
        return null;
    }

    public Faq savefaq(Faq existing) {
        return faqRepository.save(existing);
    }

    public Optional<Faq> getFaqById(Integer id) {
        return faqRepository.findById(id);
    }
}