package fr.insalyon.pld.semanticweb.controller;

import fr.insalyon.pld.semanticweb.model.persistence.ArtistModel;
import fr.insalyon.pld.semanticweb.model.persistence.MovieModel;
import fr.insalyon.pld.semanticweb.repositories.entities.Artist;
import fr.insalyon.pld.semanticweb.repositories.mapper.ArtistMapper;
import fr.insalyon.pld.semanticweb.repositories.services.ActorRepository;
import fr.insalyon.pld.semanticweb.repositories.services.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@CrossOrigin
@Controller
public class ArtistController extends AbstractController {

  @Autowired
  ArtistMapper artistMapper;

  @Autowired
  ActorRepository actorRepository;
  @Autowired
  ProducerRepository producerRepository;

  @RequestMapping(value = "/actors/unique", produces = "application/json")
  public @ResponseBody
  ArtistModel getActor(@RequestParam String uuid) {
    return fetchUniqueFromRepository(uuid, actorRepository, artistMapper);
  }

  @RequestMapping("/actors")
  public @ResponseBody
  List<ArtistModel> actorIndex(@RequestParam(value = "name", required = false) String query,
                               @RequestParam(value = "short", required = false, defaultValue = "true") String isShort) {
    return fetchFromRepository(query, isShort, actorRepository, artistMapper);
  }

  @RequestMapping("/producers")
  public @ResponseBody
  List<ArtistModel> producers(@RequestParam(value = "name", required = false) String query,
                         @RequestParam(value = "short", required = false, defaultValue = "true") String isShort) {
    return fetchFromRepository(query, isShort, producerRepository, artistMapper);
  }

  @RequestMapping(value = "/producers/unique", produces = "application/json")
  public @ResponseBody
  ArtistModel getProducer(@RequestParam String uuid) {
    return fetchUniqueFromRepository(uuid, producerRepository, artistMapper);
  }

}
