package ee.idu.vc.controllers;


import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.forms.UpdateDetailsForm;
import ee.idu.vc.forms.UpdatePasswordForm;
import ee.idu.vc.model.Account;
import ee.idu.vc.repository.AccountRepository;
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
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/account/inbox", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideAccountInboxPage() { return new ModelAndView("angular"); }

    @RequestMapping(value = "/account/inbox/message", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideAccountInboxMessagePage() { return new ModelAndView("angular"); }

    @RequestMapping(value = "/account/password", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView providePasswordResetPage() {
        return new ModelAndView("angular");
    }

    @RequireAuth
    @RequestMapping(value = "/account/password", method = RequestMethod.POST)
    public Object updatePassword(@Valid UpdatePasswordForm form, BindingResult result, @AuthAccount Account account) {
        Map<String, List<String>> errors = CVUtil.extractErrors(result);
        checkConfirmations(form, errors);
        checkPassword(form.getPassword(), errors, account);
        if (CVUtil.containsErrors(errors)) return CVUtil.jsonFailureMessageWithErrors(errors);
        account.setPasswordHash(BCrypt.hashpw(form.getNewPassword(),  BCrypt.gensalt()));
        accountRepository.update(account);
        return CVUtil.jsonSimpleSuccessMessage();
    }

    @RequireAuth
    @RequestMapping(value = "/account/mydetails", method = RequestMethod.GET, produces = "application/json")
    public Object getAccountDetails(@AuthAccount Account account) {
        return account;
    }

    @RequireAuth
    @RequestMapping(value = "/account/mydetails", method = RequestMethod.POST)
    public ObjectNode updateAccountDetails(@Valid UpdateDetailsForm form, BindingResult result,
                                       @AuthAccount Account account) {
        Map<String, List<String>> errors = CVUtil.extractErrors(result);
        checkConfirmations(form, errors);
        if (CVUtil.containsErrors(errors)) return CVUtil.jsonFailureMessageWithErrors(errors);
        account.setCompanyName(form.getOrganisation());
        account.setFirstName(form.getFirstName());
        account.setLastName(form.getLastName());
        account.setAddress(form.getAddress());
        account.setEmail(form.getEmail());
        account.setPhone(form.getTelephone());
        accountRepository.update(account);
        return CVUtil.jsonSimpleSuccessMessage();
    }

    private void checkPassword(String password, Map<String, List<String>> errors, Account account) {
        if (BCrypt.checkpw(password, account.getPasswordHash())) return;
        errors.get(CVUtil.ERROR_FIELDS).add("password");
        errors.get(CVUtil.ERROR_MESSAGES).add("Invalid password.");
    }

    private void checkConfirmations(UpdatePasswordForm form, Map<String, List<String>> errors) {
        if (Objects.equals(form.getNewPassword(), form.getNewPasswordConf())) return;
        errors.get(CVUtil.ERROR_FIELDS).add("newPasswordConf");
        errors.get(CVUtil.ERROR_MESSAGES).add("Passwords do not match.");
    }

    private void checkConfirmations(UpdateDetailsForm form, Map<String, List<String>> errors) {
        if (Objects.equals(form.getEmail(), form.getEmailConf())) return;
        errors.get(CVUtil.ERROR_FIELDS).add("emailConf");
        errors.get(CVUtil.ERROR_MESSAGES).add("Emails do not match.");
    }

    @RequestMapping(value = "/account/details", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideAccountDetailsPage() {
        return new ModelAndView("angular");
    }
}
