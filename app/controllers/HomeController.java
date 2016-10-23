package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ConnectionRequest;
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

        data.set("connectionRequests",

                objectMapper.valueToTree(

                        user.connectionRequestsRecieved.stream().filter(x->x.status.equals(ConnectionRequest.Status.WAITING)).
                                map(connectionRequestRec->{

                            ObjectNode connectionReqJson= objectMapper.createObjectNode();

                            User conReqUser = User.find.byId(connectionRequestRec.id);
                            Profile senderProfile = Profile.find.byId(connectionRequestRec.sender.profile.id);

                            connectionReqJson.put("id",connectionRequestRec.id);
                            connectionReqJson.put("email",conReqUser.email);
                            connectionReqJson.put("firstname",senderProfile.firstName);

                            return connectionReqJson;

                        }).collect(Collectors.toList())
                )
        );

        data.set("suggestions",

                objectMapper.valueToTree(

                        User.find.all().stream().filter(x->!user.equals(x)).
                                filter(x->!user.connections.contains(x)).
                                filter(x->!user.connectionRequestsRecieved.stream().map(y->y.sender).collect(Collectors.toList()).contains(x)).
                                filter(x->!user.connectionRequestsSent.stream().map(y->y.receiver).collect(Collectors.toList()).contains(x)).
                                map(suggestion->{

                                    ObjectNode suggJson= objectMapper.createObjectNode();

                                    User suggUser = User.find.byId(suggestion.id);
                                    Profile senderProfile = Profile.find.byId(suggestion.profile.id);

                                    suggJson.put("id",suggestion.id);
                                    suggJson.put("email",suggUser.email);
                                    suggJson.put("firstname",senderProfile.firstName);

                                    return suggJson;

                                }).collect(Collectors.toList())
                )
        );

        return ok(data);
    }

}
