package com.kontrol.api.user
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface UserRepository:JpaRepository<User,Long>{fun findByEmail(email:String):User?}
interface CategoryRepository:JpaRepository<Category,Long>{fun findAllByUserId(userId:Long):List<Category>;fun findByIdAndUserId(id:Long,userId:Long):Category?}
interface FinancialAccountRepository:JpaRepository<FinancialAccount,Long>{fun findAllByUserId(userId:Long):List<FinancialAccount>;fun findByIdAndUserId(id:Long,userId:Long):FinancialAccount?}
interface TransactionRepository:JpaRepository<Transaction,Long>{fun findAllByUserIdOrderByDateDescCreatedAtDesc(userId:Long):List<Transaction>;fun findAllByUserIdAndDate(userId:Long,date:LocalDate):List<Transaction>}
interface WorkScheduleRepository:JpaRepository<WorkSchedule,Long>{fun findAllByUserId(userId:Long):List<WorkSchedule>}
interface WorkRecordRepository:JpaRepository<WorkRecord,Long>{fun findByUserIdAndDate(userId:Long,date:LocalDate):WorkRecord?;fun findAllByUserIdOrderByDateDesc(userId:Long):List<WorkRecord>}
interface NoteRepository:JpaRepository<Note,Long>{fun findAllByUserIdOrderByUpdatedAtDesc(userId:Long):List<Note>;fun findByIdAndUserId(id:Long,userId:Long):Note?}
interface ShoppingListRepository:JpaRepository<ShoppingList,Long>{fun findAllByUserIdOrderByCreatedAtDesc(userId:Long):List<ShoppingList>;fun findByIdAndUserId(id:Long,userId:Long):ShoppingList?}
interface ShoppingItemRepository:JpaRepository<ShoppingItem,Long>{fun findAllByListId(listId:Long):List<ShoppingItem>;fun findByIdAndListUserId(id:Long,userId:Long):ShoppingItem?}
interface TaskRepository:JpaRepository<Task,Long>{fun findAllByUserIdOrderByDateAscCreatedAtDesc(userId:Long):List<Task>;fun findAllByUserIdAndDateOrderByCompletedAscCreatedAtDesc(userId:Long,date:LocalDate):List<Task>;fun findByIdAndUserId(id:Long,userId:Long):Task?}
