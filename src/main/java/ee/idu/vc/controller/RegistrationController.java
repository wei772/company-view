package ee.idu.vc.controller;

import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.controller.form.RegistrationForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.Account;
import ee.idu.vc.model.AccountType;
import ee.idu.vc.repository.AccountStatusRepository;
import ee.idu.vc.repository.AccountRepository;
import ee.idu.vc.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
    public ModelAndView getAngularView() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse accountRegistration(@Valid RegistrationForm form, BindingResult bindResult) {
        SimpleResponse response = new SimpleResponse(bindResult);
        if (!form.passwordsMatch()) response.addError("passwordConf", "Passwords do not match.");
        if (!form.emailsMatch()) response.addError("emailConf", "E-mails do not match.");
        if (userAlreadyExists(form.getUsername())) response.addError("username", "Username already exists.");
        if (emailAlreadyExists(form.getEmail())) response.addError("email", "E-mail already exists.");
        if (response.hasErrors()) return response;
        addAccountToDatabase(form);
        return response;
    }

    private boolean userAlreadyExists(String username) {
        return accountRepository.findByUsernameIgnoreCase(username) != null;
    }

    private boolean emailAlreadyExists(String email) {
        return accountRepository.findByEmailIgnoreCase(email) != null;
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