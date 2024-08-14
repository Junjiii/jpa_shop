package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;


    @Test
    public void 상품주문( ) throws Exception
    {
        // given
        Member member = createMember();
        Item book = createBook("JPA", 20000, 10);

        int count = 1;
        // when
        Long orderId = orderService.order(member.getId(),book.getId(), count);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1,getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(20000 * count ,getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(9,book.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다.");
    }



    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception
    {
        // given
        Member member = createMember();
        Item book = createBook("JPA", 20000, 10);
        int count = 11;

        // when
        orderService.order(member.getId(),book.getId(), count);

        // then
        fail("예외가 발생해야한다.");
    }

    @Test
    public void 주문취소() throws Exception
    {
        // given
        Member member = createMember();
        Item book = createBook("JPA", 20000, 10);
        int count = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), count);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(10, book.getStockQuantity(), "상품 주문 취소 시 개수");
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "상품 주문 상태는 CANCEL");
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book  = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Kim");
        member.setAddress(new Address("city1", "street1", 12345));
        em.persist(member);
        return member;
    }

}
