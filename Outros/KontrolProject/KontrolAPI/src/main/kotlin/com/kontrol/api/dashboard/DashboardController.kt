package com.kontrol.api.dashboard
import com.kontrol.api.auth.CurrentUser
import com.kontrol.api.user.*
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

data class FinanceSummary(val balance:Double,val expensesToday:Double)
data class WorkSummary(val workedMinutes:Int,val expectedMinutes:Int)
data class RoutineSummary(val totalTasks:Int,val completedTasks:Int)
data class ShoppingSummary(val pendingItems:Int)
data class DashboardResponse(val finance:FinanceSummary,val work:WorkSummary,val routine:RoutineSummary,val shopping:ShoppingSummary)
@RestController @RequestMapping("/api/dashboard")
class DashboardController(private val current:CurrentUser,private val tx:TransactionRepository,private val accounts:FinancialAccountRepository,private val work:WorkRecordRepository,private val tasks:TaskRepository,private val lists:ShoppingListRepository,private val items:ShoppingItemRepository){
 @GetMapping fun get():DashboardResponse{val u=current.get();val all=tx.findAllByUserIdOrderByDateDescCreatedAtDesc(u.id);val balance=accounts.findAllByUserId(u.id).sumOf{it.initialBalance}+all.sumOf{if(it.type=="INCOME")it.amount else -it.amount};val expenses=tx.findAllByUserIdAndDate(u.id,LocalDate.now()).filter{it.type=="EXPENSE"}.sumOf{it.amount};val w=work.findByUserIdAndDate(u.id,LocalDate.now());val ts=tasks.findAllByUserIdAndDateOrderByCompletedAscCreatedAtDesc(u.id,LocalDate.now());val pending=lists.findAllByUserIdOrderByCreatedAtDesc(u.id).sumOf{items.findAllByListId(it.id).count{!it.completed}};return DashboardResponse(FinanceSummary(balance,expenses),WorkSummary(w?.workedMinutes?:0,w?.expectedMinutes?:480),RoutineSummary(ts.size,ts.count{it.completed}),ShoppingSummary(pending))}
}
