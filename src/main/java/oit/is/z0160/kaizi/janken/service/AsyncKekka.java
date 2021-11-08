package oit.is.z0160.kaizi.janken.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z0160.kaizi.janken.model.Match;
import oit.is.z0160.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {
  boolean dbUpdated = false;

  @Autowired
  MatchMapper matchMapper;

  public Match syncShowMatch() {
    return matchMapper.selectByIsActive();
  }

  @Transactional
  public Match syncUpdateMatch(int id) {
    // 削除対象のフルーツを取得
    Match match = matchMapper.selectById(id);

    // 削除
    matchMapper.updateById(id);

    // 非同期でDB更新したことを共有する際に利用する
    this.dbUpdated = true;

    return match;
  }

  @Async
  public void asyncShowKekka(SseEmitter emitter) {
    dbUpdated = true;
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        // DBが更新されていれば更新後のフルーツリストを取得してsendし，1s休み，dbUpdatedをfalseにする
        Match match = this.syncShowMatch();
        emitter.send(match);
        TimeUnit.MILLISECONDS.sleep(1000);
        dbUpdated = false;
      }
    } catch (Exception e) {
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowFruitsList complete");
  }

}
