package com.kontrol.api.work
import com.kontrol.api.auth.CurrentUser
import com.kontrol.api.user.WorkRecord
import com.kontrol.api.user.WorkRecordRepository
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

data class WorkRecordRequest(
    val date : LocalDate,
    val entryTime : LocalTime?,
    val breakStart : LocalTime?,
    val breakEnd : LocalTime?,
    val exitTime : LocalTime?,
    val expectedMinutes : Int = 480
)
data class WorkRecordResponse(
    val id : Long,
    val date : LocalDate,
    val entryTime : LocalTime?,
    val breakStart : LocalTime?,
    val breakEnd : LocalTime?,
    val exitTime : LocalTime?,
    val workedMinutes : Int,
    val expectedMinutes : Int
)

@RestController
@RequestMapping("/api/work-records")
class WorkController(
    private val current : CurrentUser,
    private val repo : WorkRecordRepository){

    @GetMapping("/{date}") fun get(@PathVariable date : LocalDate) : WorkRecordResponse? {
     return repo.findByUserIdAndDate(current.get().id, date)?.toDto()
    }

    @GetMapping fun list() {
     repo.findAllByUserIdOrderByDateDesc(current.get().id).map{ it.toDto() }
    }

    @PostMapping fun create(@RequestBody r : WorkRecordRequest) {
     save(null, r)
    }

    @PutMapping("/{id}") fun update(@PathVariable id : Long, @RequestBody r : WorkRecordRequest) {
        save(id,r )
    }

    private fun save(id : Long?, r : WorkRecordRequest) : WorkRecordResponse {
        val u = current.get()
        val x = id?.let{ repo.findById(it).orElseThrow() } ?: repo.findByUserIdAndDate(u.id, r.date) ?: WorkRecord(user = u, date = r.date)

        if(x.user.id != u.id)
            throw org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)

        x.entryTime = r.entryTime
        x.breakStart = r.breakStart
        x.breakEnd = r.breakEnd
        x.exitTime = r.exitTime
        x.expectedMinutes = r.expectedMinutes
        x.workedMinutes = calc(r)

        return repo.save(x).toDto()
    }

    private fun calc(r : WorkRecordRequest) : Int {
        val start = r.entryTime ?: return 0
        val end = r.exitTime ?: return 0
        var m = Duration.between(start,end).toMinutes().toInt()

        if(r.breakStart != null && r.breakEnd != null)
            m -= Duration.between(r.breakStart, r.breakEnd).toMinutes().toInt()

        return m.coerceAtLeast(0)
    }

    private fun WorkRecord.toDto() = WorkRecordResponse(id,date,entryTime,breakStart,breakEnd,exitTime,workedMinutes,expectedMinutes)
}
