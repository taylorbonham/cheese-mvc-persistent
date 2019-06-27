package org.launchcode.controllers;


import org.hibernate.validator.constraints.NotBlank;
import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */

@Controller

@RequestMapping("cheese")

public class CheeseController {


    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;


    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");
        return "cheese/index";
    }


    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute("categories",categoryDao.findAll());
        model.addAttribute(new Cheese());
        return "cheese/add";
    }



    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors,@RequestParam int categoryId, Model model) {


        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories",categoryDao.findAll());
            return "cheese/add";
        }
        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }


    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }



    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {
        if(cheeseIds == null &&  cheeseIds.length == 0 )
            return "redirect:/cheese";

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }



    @RequestMapping(value = "category/{id}", method = RequestMethod.GET)
    public String displayAllCheeseForCat(Model model, @PathVariable int id){
        Category cat = categoryDao.findOne(id);
        if(cat!=null) {
            List<Cheese> cheeses = cat.getCheeses();
            model.addAttribute("cheeses", cheeses);
            model.addAttribute("title", "Category : " + cat.getName());
            return "cheese/index";
        }

        return "redirect:/category";

    }



    @RequestMapping(value="edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId){
        Cheese cheese = cheeseDao.findOne(cheeseId);
        if(cheese!=null) {
            model.addAttribute("title", "Edit - " + cheese.getName());
            model.addAttribute("categories",categoryDao.findAll());
            model.addAttribute("cheese", cheese);
            return "cheese/edit";
        }
        return "redirect:/cheese";

    }



    @RequestMapping(value="edit/{cheeseId}", method = RequestMethod.POST)
    public String processEditForm(Model model, @ModelAttribute @Valid Cheese cheese,@RequestParam int categoryId, @RequestParam int id, Errors errors){
        if(cheese==null && errors.hasErrors()){
            return "cheese/edit";

        }
        Cheese updateCheese = cheeseDao.findOne(id);
        Category cat = categoryDao.findOne(categoryId);
        updateCheese.setCategory(cat);
        updateCheese.setName(cheese.getName());
        updateCheese.setDescription(cheese.getDescription());
        cheeseDao.save(updateCheese);
        return "redirect:/cheese";

    }







}