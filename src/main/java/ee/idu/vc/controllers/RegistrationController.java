package ee.idu.vc.controllers;

import ee.idu.vc.forms.RegistrationForm;
import ee.idu.vc.model.AccountStatus;
import ee.idu.vc.model.User;
import ee.idu.vc.model.UserType;
import ee.idu.vc.repository.AccountStatusRepository;
import ee.idu.vc.repository.UserRepository;
import ee.idu.vc.repository.UserTypeRepository;
import ee.idu.vc.util.CVUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView provideRegistrationPage() {
        return new ModelAndView("angular");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(@Valid RegistrationForm registrationForm, BindingResult bindingResult) {
        Map<String, List<String>> errors = CVUtil.extractErrors(bindingResult);
        checkConfirmations(registrationForm, errors);
        checkUsername(registrationForm, errors);
        checkEmail(registrationForm, errors);
        if (CVUtil.containsErrors(errors)) return CVUtil.jsonFailureMessageWithErrors(errors);
        registerNewUser(registrationForm);
        return CVUtil.jsonSimpleSuccessMessage();
    }

    private void checkEmail(RegistrationForm registrationForm, Map<String, List<String>> errors) {
        if (userRepository.findByEmailIgnoreCase(registrationForm.getEmail()) == null) return;
        errors.get(CVUtil.ERROR_FIELDS).add("email");
        errors.get(CVUtil.ERROR_MESSAGES).add("E-mail already exists.");
    }

    private void checkUsername(RegistrationForm registrationForm, Map<String, List<String>> errors) {
        if (userRepository.findByUsernameIgnoreCase(registrationForm.getUsername()) == null) return;
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

    private void registerNewUser(RegistrationForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setAddress(form.getAddress());
        user.setTelephoneNumber(form.getTelephone());
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setOrganisationName(form.getOrganisation());
        user.setAccountStatus(accountStatusRepository.findByName(AccountStatus.ACTIVE));
        user.setUserType(userTypeRepository.findByName(UserType.COMPANY));
        userRepository.create(user);
    }
}