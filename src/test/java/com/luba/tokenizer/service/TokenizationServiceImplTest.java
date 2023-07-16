package com.luba.tokenizer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.luba.OpenAISpringClientApplication;
import com.luba.tokenizer.service.impl.TokenizationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {OpenAISpringClientApplication.class})
class TokenizationServiceImplTest {

    @Autowired
    private TokenizationServiceImpl tokenizationService;


    @Test
    void encode_shouldEncodeTextTo4Tokens() {
        var result = tokenizationService.getTokenCount("hello world 2 test");

        assertThat(result).isNotNull().isEqualTo(4);
    }

    @Test
    void encode_shouldEncodeTextToCorrectTokens() {
        var result = tokenizationService.getTokenCount("this text has many tokens, some which are unknown, some which we already know!");

        assertThat(result).isNotNull().isEqualTo(17);
    }

    @Test
    void encode_shouldEncodeText_thatIsLonger() {
        String input = "Many words map to one token, but some don't: indivisible.";
        var result = tokenizationService.getTokenCount(input);

        assertThat(result).isNotNull().isEqualTo(16);
    }

    @Test
    void encode_shouldEncodeText_thatContainsEmojis() {
        String input = "hello 👋 world 🌍! this is a crazy time we live in, woohoo. adsdads dsfasdfa code";
        var result = tokenizationService.getTokenCount(input);

        assertThat(result).isNotNull().isEqualTo(30);
    }

    @Test
    void encode_longText() {
        String input = "Certainly! Based on your preferences for classic literature and psychological horror, here are some book recommendations that might suit your reading level:\n"
            + "\n"
            + "1. \"Frankenstein\" by Mary Shelley - A classic novel that combines elements of horror, science fiction, and exploration of human nature.\n"
            + "2. \"Dracula\" by Bram Stoker - Another classic that delves into psychological horror and the iconic vampire lore.\n"
            + "3. \"The Strange Case of Dr. Jekyll and Mr. Hyde\" by Robert Louis Stevenson - A psychological thriller exploring the duality of human nature.\n"
            + "4. \"Rebecca\" by Daphne du Maurier - A Gothic suspense novel that combines romance and psychological tension.\n"
            + "5. \"The Picture of Dorian Gray\" by Oscar Wilde - A philosophical novel that delves into the dark side of human desires and the pursuit of eternal youth.\n"
            + "\n"
            + "While these books are considered classics, they also incorporate psychological and horror elements that you enjoy. Remember to check the specific Lexile level of the book editions you choose, as different editions may have variations in Lexile scores.\n"
            + "\n"
            + "I hope you find these recommendations intriguing and enjoy exploring these literary works! Let me know if there's anything else I can assist you with. Happy reading!";
        var result = tokenizationService.getTokenCount(input);

        assertThat(result).isNotNull().isEqualTo(256);
    }

}
