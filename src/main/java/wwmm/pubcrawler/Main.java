/*
 * Copyright 2010-2011 Nick Day, Sam Adams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwmm.pubcrawler;

import wwmm.pubcrawler.crawlers.acs.AcsJournalCrawler;
import wwmm.pubcrawler.crawlers.acta.ActaJournalCrawler;
import wwmm.pubcrawler.crawlers.chemsocjapan.ChemSocJapanJournalCrawler;
import wwmm.pubcrawler.crawlers.nature.NatureJournalCrawler;
import wwmm.pubcrawler.crawlers.rsc.RscJournalCrawler;

/**
 * @author Sam Adams
 */
public class Main {



    public static void main(String[] args) {

        new Thread(){
            @Override
            public void run() {
                try {
                    AcsJournalCrawler.main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    ActaJournalCrawler.main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    ChemSocJapanJournalCrawler.main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    NatureJournalCrawler.main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    RscJournalCrawler.main(new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}
