package com.nishant.code_it.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
   private Long qid;
   private Long attemptId;
   private String result;
   private String message;
}
