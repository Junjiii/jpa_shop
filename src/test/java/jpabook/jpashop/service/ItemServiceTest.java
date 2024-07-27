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
        itemService.saveItem(one);
        Item two = itemService.findOne(id);
        System.out.println("two = " + two);
        // then
//        Assertions.assertEquals(item,one);
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

    @Test
    public void itemDuplicateidTest() throws Exception {
        // given
        Book book1 = new Book();
        book1.setName("test1");

        itemRepository.save(book1);

        List<Item> items = itemService.findItems();
        int size1 = items.size();
        Long id = items.get(0).getId();
        String name = items.get(0).getName();

        System.out.println("items = " + items.get(0));
        System.out.println("id = " + id);
        System.out.println("name = " + name);

        Book book2 = new Book();
        book2.setId(id);
        book2.setName(name);
        itemRepository.save(book2);

        List<Item> items2 = itemService.findItems();
        int size2 = items2.size();
        Long id2 = items2.get(0).getId();
        String name2 = items2.get(0).getName();

        System.out.println("items2 = " + items2.get(0));
        System.out.println("id2 = " + id2);
        System.out.println("name2 = " + name2);


        // when

        // then
        Assertions.assertEquals(size1,size2);
    }
}