package com.hsb.note.dao;

import com.hsb.note.pojo.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author SipooHe
 * @date 2020/3/26 17:53
 */
public interface NoteDao extends JpaRepository<Note, Integer> {

    List<Note> findAllByCategoryId(int id);
}
