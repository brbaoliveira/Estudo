package com.kontrol.api.note
import com.kontrol.api.auth.CurrentUser
import com.kontrol.api.user.*
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

data class NoteRequest(val title:String,val content:String,val favorite:Boolean=false)
data class NoteResponse(val id:Long,val title:String,val content:String,val favorite:Boolean,val createdAt:LocalDateTime,val updatedAt:LocalDateTime)
@RestController @RequestMapping("/api/notes")
class NoteController(private val current:CurrentUser,private val repo:NoteRepository){
 @GetMapping fun all()=repo.findAllByUserIdOrderByUpdatedAtDesc(current.get().id).map{it.dto()}
 @PostMapping fun create(@RequestBody r:NoteRequest)=repo.save(Note(user=current.get(),title=r.title,content=r.content,favorite=r.favorite)).dto()
 @PutMapping("/{id}") fun update(@PathVariable id:Long,@RequestBody r:NoteRequest):NoteResponse{val x=repo.findByIdAndUserId(id,current.get().id)?:throw org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);x.title=r.title;x.content=r.content;x.favorite=r.favorite;x.updatedAt=LocalDateTime.now();return repo.save(x).dto()}
 @DeleteMapping("/{id}") fun delete(@PathVariable id:Long){repo.findByIdAndUserId(id,current.get().id)?.let{repo.delete(it)}}
 private fun Note.dto()=NoteResponse(id,title,content,favorite,createdAt,updatedAt)
}
