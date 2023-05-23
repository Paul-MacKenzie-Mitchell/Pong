import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends JFrame implements Runnable {
    public Graphics2D g2;
    public KL keyListener = new KL();
    public Rect player1, ai, ballRect;
    public Ball ball;
    public PlayerController playerController;
    public AIController aiController;
    public Text leftScoreText, rightScoreText;
    public boolean isRunning = true;

    public Window(){
        this.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(keyListener);
        Constants.TOOLBAR_HEIGHT = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;

        g2 = (Graphics2D) this.getGraphics();
        player1 = new Rect(Constants.HZ_PADDING,
                Constants.SCREEN_HEIGHT /2 - Constants.PADDLE_HEIGHT / 2,Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT,Constants.PADDLE_COLOR);
        playerController = new PlayerController(player1, keyListener);

        ai = new Rect(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HZ_PADDING,
                Constants.SCREEN_HEIGHT /2 - Constants.PADDLE_HEIGHT / 2,
                Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT,
                Constants.PADDLE_COLOR);

        leftScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.TEXT_X_POS,Constants.TEXT_Y_POS);
        rightScoreText = new Text(0, new Font("Times New Roman", Font.PLAIN, Constants.TEXT_SIZE),
                Constants.SCREEN_WIDTH - Constants.TEXT_X_POS - Constants.TEXT_SIZE,Constants.TEXT_Y_POS);

        ballRect =  new Rect(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2, Constants.BALL_SIZE,
                Constants.BALL_SIZE, Constants.BALL_COLOR);
        ball =  new Ball(ballRect, player1, ai, leftScoreText, rightScoreText);

        aiController = new AIController(new PlayerController(ai), ballRect);
    }

    public void update(double dt) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);

        playerController.update(dt);
        aiController.update(dt);
        ball.update(dt);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Constants.WINDOW_COLOR);
        g2.fillRect(0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        leftScoreText.draw(g2);
        rightScoreText.draw(g2);

        player1.draw(g2);
        ai.draw(g2);
        ballRect.draw(g2);
    }
    public void stop() {
        isRunning = false;
    }
    @Override
    public void run() {
        double lastFrameTime = 0.0;
        while (isRunning) {
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            update(deltaTime);
        }
        this.dispose();
        return;
    }


}
