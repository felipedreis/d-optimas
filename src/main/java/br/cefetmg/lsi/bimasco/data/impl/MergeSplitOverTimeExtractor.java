package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.MessageState;
import br.cefetmg.lsi.bimasco.persistence.dao.MessageStateDAO;

import java.util.Comparator;
import java.util.List;

public class MergeSplitOverTimeExtractor extends Extractor<MessageState> {

    private MessageStateDAO messageStateDAO;

    public MergeSplitOverTimeExtractor(String problem, MessageStateDAO messageStateDAO) {
        super(problem);
        this.messageStateDAO = messageStateDAO;
    }

    @Override
    public List<MessageState> getData() {
        List<MessageState> mergesResult = messageStateDAO.findByProblemAndType(problem,"MergeResult").all();
        List<MessageState> splits =  messageStateDAO.findByProblemAndType(problem, "RegionSplit").all();
        splits.addAll(mergesResult);
        splits.sort(Comparator.comparing(MessageState::getTime));
        return splits;
    }

    @Override
    public String formatDataToCsv(MessageState messageState) {
        return String.format("%d, %s, %d, %s, %s, %s\n", messageState.getTime(), messageState.getId(),
                messageState.getTimestamp(), messageState.getMessageType(), messageState.getStatus(),
                messageState.getEntityId());
    }

    @Override
    public String getFileName() {
        return "mergeSplitOverTime";
    }
}
