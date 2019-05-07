package frame;

import panel.*;

import java.awt.*;

public class AdminFrame extends MainFrame {
    @Override
    protected NoticePanel newInstanceNoticePanel() {
        return new AdminNoticePanel();
    }

    @Override
    protected FilePanel newInstanceFilePanel() {
        return new AdminFilePanel();
    }

    @Override
    protected VotePanel newInstanceVotePanel() {
        return new AdminVotePanel();
    }
}
