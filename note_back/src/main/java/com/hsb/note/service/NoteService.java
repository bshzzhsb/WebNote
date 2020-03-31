package com.hsb.note.service;

import com.hsb.note.dao.NoteDao;
import com.hsb.note.pojo.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SipooHe
 * @date 2020/3/27 11:18
 */
@Service
public class NoteService {

    @Autowired
    NoteDao noteDao;

    public List<Note> getAll() {
        return noteDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Note> getNotesByCategoryId(int id) {
        return noteDao.findAllByCategoryId(id);
    }

    public Note getNoteById(int id) {
        return noteDao.findById(id).get();
    }

    @Transactional
    public void updateNoteInfo(Note note) {
        noteDao.save(note);
    }

    public void deleteById(int id) {
        noteDao.deleteById(id);
    }
}
