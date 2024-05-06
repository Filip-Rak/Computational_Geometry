package pau.pau5;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FaviconController
{
    @RequestMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon()
    {
        // No action, just to suppress the 500 error in logs for favicon.ico
    }
}
