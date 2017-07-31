package cn.lmjia.market.core.trj;

import cn.lmjia.market.core.entity.trj.AuthorisingInfo;
import me.jiangcai.payment.PaymentForm;
import me.jiangcai.payment.event.OrderPaySuccess;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 投融家相关服务，我们也认可它是一种支付方式
 *
 * @author CJ
 */
public interface TRJService extends PaymentForm {

    /**
     * 添加一个有效按揭码
     *
     * @param authorising
     * @param idNumber
     */
    @Transactional
    void addAuthorisingInfo(String authorising, String idNumber);

    void deliverUpdate(Number orderId, String authorising, String deliverCompany, String deliverStore
            , Number stockQuantity, String shipmentTime, String deliverTime, String name, String mobile, String address
            , String orderTime) throws IOException;

    void submitOrderInfo(String authorising, Number orderId, String name, String idNumber, String mobile
            , String goodCode, String goodName, Number amount, String dueAmount, String address, String orderTime
            , Number recommendCode) throws IOException;

    @EventListener(OrderPaySuccess.class)
    void paySuccess(OrderPaySuccess event);

    /**
     * 检查可用的按揭码
     *
     * @param authorising
     * @param idNumber
     * @return
     * @throws InvalidAuthorisingException
     */
    @Transactional(readOnly = true)
    AuthorisingInfo checkAuthorising(String authorising, String idNumber) throws InvalidAuthorisingException;

    /**
     * 物流信息更新
     *
     * @param orderId        订单号
     * @param deliverCompany 物流公司
     * @param deliverStore   物流仓库
     * @param stockQuantity  之后的剩余库存
     * @param shipmentTime   发货时间
     * @param deliverTime    送达时间
     */
    void deliverUpdate(long orderId, String deliverCompany, String deliverStore, int stockQuantity
            , LocalDate shipmentTime, LocalDate deliverTime);
}
