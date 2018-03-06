package Entities;

import javax.persistence.*;

/**
 * Created by Лютий on 03.03.2018.
 */
@Entity
@Table(name = "notes", schema = "telegram")
public class NotesEntity {
    private int noteId;
    private String note;


    @JoinColumn(referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = UserEntity.class,cascade = CascadeType.ALL)
    private long userId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public  NotesEntity (){}

    public NotesEntity( String note, long userId) {
        this.noteId = noteId;
        this.note = note;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "NotesEntity{" +
                "noteId=" + noteId +
                ", note='" + note + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotesEntity that = (NotesEntity) o;

        if (noteId != that.noteId) return false;
        if (note != null ? !note.equals(that.note) : that.note != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = noteId;
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}
