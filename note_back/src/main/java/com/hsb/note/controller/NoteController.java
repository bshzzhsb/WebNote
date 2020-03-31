package com.hsb.note.controller;

import com.hsb.note.pojo.Note;
import com.hsb.note.response.Response;
import com.hsb.note.service.CategoryServicce;
import com.hsb.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author SipooHe
 * @date 2020/3/26 16:53
 */
@Controller
public class NoteController {

    @Autowired
    CategoryServicce categoryServicce;
    @Autowired
    NoteService noteService;

    @CrossOrigin
    @GetMapping("/api/notes")
    @ResponseBody
    public List<Note> getNoteList() {
        return noteService.getAll();
    }

    @CrossOrigin
    @GetMapping("/api/note/{id}")
    @ResponseBody
    public Response getNote(@PathVariable("id") int id) {
        Note note = noteService.getNoteById(id);
        return new Response(200, "成功", note);
    }

    @CrossOrigin
    @GetMapping("/api/categories/{id}/notes")
    @ResponseBody
    public List<Note> getNotesByCategoryId(@PathVariable("id") int id) {
        return noteService.getNotesByCategoryId(id);
    }

    @CrossOrigin
    @PostMapping("/api/update/{type}/note/{id}")
    @ResponseBody
    public Response updateNoteInfo(@RequestBody Note requestNote, @PathVariable("id") int id,
                                   @PathVariable("type") String type) {
        Note note = noteService.getNoteById(id);
        if (type.equals("info")) {
            note.setName(requestNote.getName());
            note.setAbs(requestNote.getAbs());
        } else if (type.equals("content")) {
            note.setContentHtml(requestNote.getContentHtml());
            note.setContentMd(requestNote.getContentMd());
        }
        note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        noteService.updateNoteInfo(note);
        return new Response(200, "success", null);
    }

    @CrossOrigin
    @PostMapping("/api/update/categories/{id}/note/add")
    @ResponseBody
    public Response addNote(@RequestBody Note requestNote, @PathVariable("id") int id) {
        Note note = new Note();
        note.setName(requestNote.getName());
        note.setAbs(requestNote.getAbs());
        note.setCategory(categoryServicce.getCategoryById(id));
        note.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        noteService.updateNoteInfo(note);
        return new Response(200, "success", null);
    }

    @CrossOrigin
    @GetMapping("/api/note/delete/{id}")
    @ResponseBody
    public Response deleteNode(@PathVariable("id") int id) {
        noteService.deleteById(id);
        return new Response(200, "success", null);
    }

}
