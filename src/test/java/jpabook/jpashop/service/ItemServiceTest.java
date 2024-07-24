package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    
    @Test
    public void 상품_저장() {
        // given
        Item item = new Book();

        // when
        itemService.saveItem(item);
        Long id = item.getId();
        System.out.println("객체 생성 후 item = " + item);
        System.out.println("save 후 id =  " + id);

        Item one = itemService.findOne(id);
        System.out.println("조회 후 one = " + one);
        // then
        Assertions.assertEquals(item,one);
    }

    @Test
    public void 모든_상품_조회() {
        // given
        Item itemA = new Book();
        Item itemB = new Album();
        Item itemC = new Movie();

        itemService.saveItem(itemA);
        itemService.saveItem(itemB);
        itemService.saveItem(itemC);

        // when
        List<Item> items = itemService.findItems();
        Long AId = items.get(0).getId();
        Long BId = items.get(1).getId();
        Long CId = items.get(2).getId();


        // then
        Assertions.assertEquals(itemA.getId(),AId);
        Assertions.assertEquals(itemB.getId(),BId);
        Assertions.assertEquals(itemC.getId(),CId);
    }
}