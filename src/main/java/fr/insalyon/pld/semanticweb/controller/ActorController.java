package fr.insalyon.pld.semanticweb.controller;

import fr.insalyon.pld.semanticweb.entities.Artist;
import fr.insalyon.pld.semanticweb.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@CrossOrigin
@Controller
public class ActorController {
    @Autowired
    ActorRepository actorRepository;

    @RequestMapping("/actors")
    public @ResponseBody
    List<Artist> index(@RequestParam(value = "name", required = false) String query,
                       @RequestParam(value = "short", required = false, defaultValue = "true") String isShort){
        if (null == query) {
            return actorRepository.findAll();
        } else {
            return actorRepository.findByName(query);
        }
    }
}
