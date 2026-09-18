package com.kontrol.api.routine
import com.kontrol.api.auth.CurrentUser
import com.kontrol.api.user.*
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

data class TaskRequest(val title:String,val description:String?,val date:LocalDate,val priority:String="NORMAL",val completed:Boolean=false)
data class TaskResponse(val id:Long,val title:String,val description:String?,val date:LocalDate,val priority:String,val completed:Boolean)
@RestController @RequestMapping("/api/tasks")
class TaskController(private val current:CurrentUser,private val repo:TaskRepository){
 @GetMapping fun all()=repo.findAllByUserIdOrderByDateAscCreatedAtDesc(current.get().id).map{it.dto()}
 @PostMapping fun create(@RequestBody r:TaskRequest)=repo.save(Task(user=current.get(),title=r.title,description=r.description,date=r.date,priority=r.priority,completed=r.completed)).dto()
 @PutMapping("/{id}") fun update(@PathVariable id:Long,@RequestBody r:TaskRequest):TaskResponse{val x=repo.findByIdAndUserId(id,current.get().id)?:throw org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);x.title=r.title;x.description=r.description;x.date=r.date;x.priority=r.priority;x.completed=r.completed;x.updatedAt=LocalDateTime.now();return repo.save(x).dto()}
 @DeleteMapping("/{id}") fun delete(@PathVariable id:Long){repo.findByIdAndUserId(id,current.get().id)?.let{repo.delete(it)}}
 private fun Task.dto()=TaskResponse(id,title,description,date,priority,completed)
}
