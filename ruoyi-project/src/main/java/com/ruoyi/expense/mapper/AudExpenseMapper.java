package com.ruoyi.expense.mapper;

import com.ruoyi.contract.domain.AudContract;
import com.ruoyi.expense.domain.AudExpense;
import java.util.List;

public interface AudExpenseMapper {

    List<AudExpense> selectExpenseTijiaoren(AudExpense record);

    int deleteByPrimaryKey(Integer expensesheetid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Aud_Expense
     *
     * @mbg.generated Sun Feb 21 22:08:20 CST 2021
     */
    int insert(AudExpense record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Aud_Expense
     *
     * @mbg.generated Sun Feb 21 22:08:20 CST 2021
     */
    AudExpense selectByPrimaryKey(Integer expensesheetid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Aud_Expense
     *
     * @mbg.generated Sun Feb 21 22:08:20 CST 2021
     */
    List<AudExpense> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table Aud_Expense
     *
     * @mbg.generated Sun Feb 21 22:08:20 CST 2021
     */
    int updateByPrimaryKey(AudExpense record);
}