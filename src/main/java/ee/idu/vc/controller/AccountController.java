package ee.idu.vc.controller;

import ee.idu.vc.auth.AuthAccount;
import ee.idu.vc.auth.RequireAuth;
import ee.idu.vc.controller.response.JsonResponse;
import ee.idu.vc.controller.response.SimpleResponse;
import ee.idu.vc.controller.form.UpdateDetailsForm;
import ee.idu.vc.controller.form.UpdatePasswordForm;
import ee.idu.vc.model.Account;
import ee.idu.vc.repository.AccountRepository;
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
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = {"/account/password", "/account/details"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAngularView() {
        return new ModelAndView("angular");
    }

    @RequireAuth
    @RequestMapping(value = "/account/password", method = RequestMethod.POST, produces = "application/json")
    public JsonResponse updatePassword(@Valid UpdatePasswordForm form, BindingResult bindResult, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bindResult);
        if (!BCrypt.checkpw(form.getPassword(), account.getPasswordHash())) response.addError("password", "Invalid password.");
        if (response.hasErrors()) return response;
        account.setPasswordHash(BCrypt.hashpw(form.getNewPassword(),  BCrypt.gensalt()));
        accountRepository.update(account);
        return response;
    }

    @RequireAuth
    @RequestMapping(value = "/account/mydetails", method = RequestMethod.GET, produces = "application/json")
    public Object getAccountDetails(@AuthAccount Account account) {
        return account;
    }

    @RequireAuth
    @RequestMapping(value = "/account/mydetails", method = RequestMethod.POST, produces = "application/json")
    public JsonResponse updateAccountDetails(@Valid UpdateDetailsForm form, BindingResult bindResult, @AuthAccount Account account) {
        SimpleResponse response = new SimpleResponse(bindResult);
        if (response.hasErrors()) return response;
        updateAccountInDB(account, form);
        return response;
    }

    private void updateAccountInDB(Account account, UpdateDetailsForm form) {
        account.setCompanyName(form.getOrganisation());
        account.setFirstName(form.getFirstName());
        account.setLastName(form.getLastName());
        account.setAddress(form.getAddress());
        account.setEmail(form.getEmail());
        account.setPhone(form.getTelephone());
        accountRepository.update(account);
    }
}