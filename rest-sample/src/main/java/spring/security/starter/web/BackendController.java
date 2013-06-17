package spring.security.starter.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.security.starter.service.BackendService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RequestMapping("/backend/**")
@Controller
public class BackendController {

    protected static Log log = LogFactory.getLog(BackendController.class);

    @Resource
    protected BackendService backendService;

    @RequestMapping(method = {RequestMethod.HEAD,RequestMethod.GET}, value="/password")
   	public @ResponseBody String echo() {
   		return "The password is: " + backendService.preAuthorizeForAuthenticated() + "\n";
   	}

    @RequestMapping(method = RequestMethod.GET, value = "secured")
    public String secured(ModelMap modelMap) {
        String message = backendService.securedForAdmin();
        modelMap.put("message", message);
        return "backend/secured";
    }

    @RequestMapping(method = RequestMethod.GET, value = "preauth")
    public String preauth(ModelMap modelMap) {
        String message = backendService.preAuthorizeForAdmin();
        modelMap.put("message", message);
        return "backend/secured";
    }

    @RequestMapping(method = RequestMethod.GET, value = "postauth")
    public String postauth(ModelMap modelMap) {
        String message = backendService.postAuthorizeForAdmin();
        modelMap.put("message", message);
        return "backend/secured";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        modelMap.put("authentication", authentication);
        return "backend/index";
    }

}
