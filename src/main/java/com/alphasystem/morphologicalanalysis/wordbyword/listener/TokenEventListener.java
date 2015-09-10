package com.alphasystem.morphologicalanalysis.wordbyword.listener;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.persistence.mongo.repository.DocumentEventListener;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class TokenEventListener extends DocumentEventListener<Token> {
}
