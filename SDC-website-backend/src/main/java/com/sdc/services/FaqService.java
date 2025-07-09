package com.sdc.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdc.entity.Faq;
import com.sdc.models.FaqModel;
import com.sdc.repo.FaqRepository;


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

    public void deleteFaq(Integer id) {
        faqRepository.deleteById(id);
    }

    public Faq updateFaq(Integer id, FaqModel faqModel) {
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

    public boolean deleteFaqById(Integer id) {
        if (faqRepository.existsById(id)) {
            faqRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


