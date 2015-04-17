package ee.idu.vc.controllers;

import ee.idu.vc.forms.RegistrationForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.AccountType;
import ee.idu.vc.repository.AccountStatusRepository;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.AccountTypeRepository;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class RegistrationController {
    @Autowired
    private AccountStatusRepository accountStatusRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideRegistrationPage() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(@Valid RegistrationForm form, BindingResult bindingResult) {
        Map<String, List<String>> errors = CVUtil.extractErrors(bindingResult);
        checkConfirmations(form, errors);
        checkUsername(form, errors);
        checkEmail(form, errors);
        if (CVUtil.containsErrors(errors)) return CVUtil.jsonFailureMessageWithErrors(errors);
        addAccountToDatabase(form);
        return CVUtil.jsonSimpleSuccessMessage();
    }

    private void checkEmail(RegistrationForm form, Map<String, List<String>> errors) {
        if (accountRepository.findByEmailIgnoreCase(form.getEmail()) == null) return;
        errors.get(CVUtil.ERROR_FIELDS).add("email");
        errors.get(CVUtil.ERROR_MESSAGES).add("E-mail already exists.");
    }

    private void checkUsername(RegistrationForm form, Map<String, List<String>> errors) {
        if (accountRepository.findByUsernameIgnoreCase(form.getUsername()) == null) return;
        errors.get(CVUtil.ERROR_FIELDS).add("username");
        errors.get(CVUtil.ERROR_MESSAGES).add("Username already exists.");
    }

    private void checkConfirmations(RegistrationForm form, Map<String, List<String>> errors) {
        if (!Objects.equals(form.getPassword(), form.getPasswordConf())) {
            errors.get(CVUtil.ERROR_FIELDS).add("passwordConf");
            errors.get(CVUtil.ERROR_MESSAGES).add("Passwords do not match.");
        }

        if (!Objects.equals(form.getEmail(), form.getEmailConf())) {
            errors.get(CVUtil.ERROR_FIELDS).add("emailConf");
            errors.get(CVUtil.ERROR_MESSAGES).add("E-mails do not match.");
        }
    }

    private void addAccountToDatabase(RegistrationForm form) {
        Account account = new Account();
        account.setUsername(form.getUsername());
        account.setPasswordHash(BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()));
        account.setAddress(form.getAddress());
        account.setPhone(form.getTelephone());
        account.setEmail(form.getEmail());
        account.setFirstName(form.getFirstName());
        account.setLastName(form.getLastName());
        account.setCompanyName(form.getOrganisation());
        account.setAccountStatus(accountStatusRepository.findByName(AccountStatus.ACTIVE));
        account.setAccountType(accountTypeRepository.findByName(AccountType.COMPANY));
        accountRepository.create(account);
    }
}