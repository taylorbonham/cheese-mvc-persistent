package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;



@Controller
@RequestMapping(value = "menu")
public class MenuController {
    @Autowired
    MenuDao menuDao;
    @Autowired
    CheeseDao cheeseDao;


    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("title", "Menu");
        model.addAttribute("menus", menuDao.findAll());
        return "menu/index";
    }


    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute(new Menu());
        return "menu/add";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAdd(Model model, @ModelAttribute @Valid Menu newMenu, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute(new Menu());
            return "menu/add";
        }


        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }


    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable @NotNull int id){
        Menu menu = menuDao.findOne(id);
        if(menu != null){
            model.addAttribute("title", "Menu : "+menu.getName()+" Items ");
            model.addAttribute(menu);
            return "menu/view";
        }
        return "menu/view";

    }


    @RequestMapping(value="/add-item/{id}", method = RequestMethod.GET)
    public String addMenuItem(Model model,@PathVariable int id){
        Menu menu = menuDao.findOne(id);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        AddMenuItemForm menuItemForm = new AddMenuItemForm(menu,cheeses);
        if(menu != null){
            model.addAttribute("title","Add item to menu: " + menu.getName());
            model.addAttribute("menuItemForm", menuItemForm);
            return "menu/add-item";
        }
        return "redirect:";

    }



    @RequestMapping(value="/add-item", method = RequestMethod.GET)
    public String addMenuItem(){
        return "redirect:/menu/";
    }


    @RequestMapping(value="/add-item/{id}", method = RequestMethod.POST)
    public String processAddMenuItem(Model model, @ModelAttribute @Valid AddMenuItemForm menuItemForm, @RequestParam int menuId, @RequestParam int cheeseId, @PathVariable int id, Errors errors){
        Menu menu = menuDao.findOne(menuId);
        if(errors.hasErrors()){
            model.addAttribute("title","Add item to menu: " + menu.getName());
            model.addAttribute("menuItemForm", menuItemForm);
            return "menu/add-item";
        }

        Cheese cheese = cheeseDao.findOne(menuItemForm.getCheeseId());
        menu.addMenuItem(cheese);
        menuDao.save(menu);
        return "redirect:/menu/view/"+menu.getId();

    }



}