package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Profile;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by lubuntu on 10/23/16.
 */
public class HomeController extends Controller {

    @Inject
    ObjectMapper objectMapper;

    public Result getProfile(Long uid){

        User user= User.find.byId(uid);
        Profile profile =Profile.find.byId(user.profile.id);

        ObjectNode data= objectMapper.createObjectNode();

        data.put("id",user.id);
        data.put("email",user.email);
        data.put("firstname",profile.firstName);
        data.put("lastname",profile.lastName);
        data.put("company",profile.company);

        data.set("connections",

                objectMapper.valueToTree(

                        user.connections.stream().map(connection->{

                            ObjectNode connectionJson= objectMapper.createObjectNode();

                            User conUser = User.find.byId(connection.id);
                            Profile conProfile = Profile.find.byId(connection.profile.id);

                            connectionJson.put("id",conUser.id);
                            connectionJson.put("email",conUser.email);
                            connectionJson.put("firstname",conProfile.firstName);
                            connectionJson.put("latname",conProfile.lastName);
                            connectionJson.put("company",conProfile.company);

                            return connectionJson;

                        }).collect(Collectors.toList())
                )

        );

        return ok();
    }

}
