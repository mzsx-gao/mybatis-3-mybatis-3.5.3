package my_demo.mapper;

import my_demo.entity.TJobHistory;
import my_demo.entity.TUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TJobHistoryMapper {

    List<TJobHistory> selectByUserId(int userId);
    
}