package com.example.anisha.mefydoctor.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anisha.mefydoctor.R;
import com.example.anisha.mefydoctor.constant.APPConstant;
import com.example.anisha.mefydoctor.handler.HttpHandler;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.model.CallModel;
import com.example.anisha.mefydoctor.model.TokenDataModel;
import com.example.anisha.mefydoctor.utility.CameraCapturerCompat;
import com.twilio.video.AudioCodec;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.EncodingParameters;
import com.twilio.video.G722Codec;
import com.twilio.video.H264Codec;
import com.twilio.video.IsacCodec;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.OpusCodec;
import com.twilio.video.PcmaCodec;
import com.twilio.video.PcmuCodec;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoCodec;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;
import com.twilio.video.Vp8Codec;
import com.twilio.video.Vp9Codec;

import java.util.Collections;

public class VideoActivity extends AppCompatActivity {

    private AudioCodec audioCodec;
    private VideoCodec videoCodec;

    private Room room;
    private LocalParticipant localParticipant;

    private static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private VideoRenderer localVideoView;
    private AudioManager audioManager;
    private VideoView primaryVideoView;
    private VideoView thumbnailVideoView;
    private EncodingParameters encodingParameters;
    private CameraCapturerCompat cameraCapturerCompat;
    private int previousAudioMode;
    private boolean previousMicrophoneMute;
    private boolean disconnectedFromOnDestroy;
    private TextView videoStatusTextView;
    private FloatingActionButton connectActionFab;
    private FloatingActionButton switchCameraActionFab;
    private FloatingActionButton localVideoActionFab;
    private FloatingActionButton muteActionFab;
    private FloatingActionButton moreActionFab;
    private String remoteParticipantIdentity;
    private SharedPreferences preferences;

    private FrameLayout mFramePlayer;
    private String accessToken;
    private String value;
    private String room_name, fcm, u_name;
    private String value_send;
    private Context context;
    FloatingActionButton actionClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoStatusTextView = findViewById(R.id.video_status_textview);
        primaryVideoView = findViewById(R.id.primary_video_view);
        thumbnailVideoView = findViewById(R.id.thumbnail_video_view);
        context = this;


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*// to wake up screen
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire();
        if ((wakeLock != null) &&           // we have a WakeLock
                (wakeLock.isHeld() == false)) {  // but we don't hold it
            wakeLock.acquire();
        }*/


        // to release screen lock
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

/*// Share your camera
        CameraCapturer cameraCapturer = new CameraCapturer(this, CameraCapturer.CameraSource.FRONT_CAMERA);
        localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturer);

// Render camera to a view
        primaryVideoView = findViewById(R.id.primary_video_view);
        thumbnailVideoView = findViewById(R.id.thumbnail_video_view);
        // Mirror front camera
        primaryVideoView.setMirror(false);

// Render camera to view
        localVideoTrack.addRenderer(primaryVideoView);

// Switch the camera source
        CameraCapturer.CameraSource cameraSource = cameraCapturer.getCameraSource();
        cameraCapturer.switchCamera();
        primaryVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.FRONT_CAMERA);
*/
        //Get AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);

//        localAudioTrack = LocalAudioTrack.create(context, true, LOCAL_AUDIO_TRACK_NAME);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        /*
         * Needed for setting/abandoning audio focus during call
         */

        Toast.makeText(this, "Hay", Toast.LENGTH_SHORT).show();


        Log.d("VActi", "onCreate | createAudioAndVideoTracks");

        /*
         * Check camera and microphone permissions. Needed in Android M.
         */
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone();
        } else
            createAudioAndVideoTracks();

        /*
         * Set the initial state of the UI
         */
        intializeUI();
        Intent i = getIntent();
        room_name = i.getExtras().getString("room");
        System.out.println("Video | onCreate | room_name: " + room_name);
        accessToken = i.getExtras().getString("token");
        System.out.println("VideoActivity | onCreate | accessToken:" + accessToken);
        connectToRoom(room_name);
    }

    private void intializeUI() {

        PackageManager packageManager = getApplicationContext().getPackageManager();
        mFramePlayer = findViewById(R.id.fragment);
        actionClose = findViewById(R.id.action_close);
        switchCameraActionFab = findViewById(R.id.switch_camera_action_fab);
        localVideoActionFab = findViewById(R.id.local_video_action_fab);
        muteActionFab = findViewById(R.id.mute_action_fab);
        moreActionFab = findViewById(R.id.more_action_fab);
        switchCameraActionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraCapturerCompat != null) {
                    CameraCapturer.CameraSource cameraSource = cameraCapturerCompat.getCameraSource();
                    cameraCapturerCompat.switchCamera();
                    if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                        thumbnailVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);
                    } else {
                        primaryVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);
                    }
                }
            }
        });
        localVideoActionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localVideoTrack != null) {
                    boolean enable = !localVideoTrack.isEnabled();
                    localVideoTrack.enable(enable);
                    int icon;
                    if (enable) {
                        icon = R.drawable.ic_videocam_white_24dp;
                        switchCameraActionFab.show();
                    } else {
                        icon = R.drawable.ic_videocam_off_black_24dp;
                        switchCameraActionFab.hide();
                    }
                    localVideoActionFab.setImageDrawable(
                            ContextCompat.getDrawable(VideoActivity.this, icon));
                }
            }
        });

        muteActionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localAudioTrack != null) {
                    boolean enable = !localAudioTrack.isEnabled();
                    localAudioTrack.enable(enable);
                    int icon = enable ?
                            R.drawable.ic_mic_white_24dp : R.drawable.ic_mic_off_white_24dp;
                    muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                            VideoActivity.this, icon));
                }
            }
        });
        moreActionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    System.out.println("VideoActivity | OnClick | PIP Check:" + PackageManager.FEATURE_PICTURE_IN_PICTURE);
                    System.out.println("VideoActivity | OnClick | PIP Check:" + packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE));
                    if (packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {


                        //enterPictureInPictureMode();

                        if (android.os.Build.VERSION.SDK_INT >= 26) {
                            //Trigger PiP mode
                            try {
                                Rational rational = new Rational(mFramePlayer.getWidth(),
                                        mFramePlayer.getHeight());

                                PictureInPictureParams mParams =
                                        new PictureInPictureParams.Builder().setAspectRatio(rational).build();

                                enterPictureInPictureMode(mParams);
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("VideoActivity | OnClick | PIP Call");
                    } else {
                        System.out.println("VideoActivity | OnClick | PIP not Called");
                        //Intent Call for not using 26
                        /*Intent intent = new Intent(VideoActivity.this, MainActivity.class);
                        startActivity(intent);*/

                    }
                } else {
                    Toast.makeText(VideoActivity.this, "Video Paused", Toast.LENGTH_SHORT).show();
                    //Intent Call for not using 26
                    /*Intent intent = new Intent(VideoActivity.this, MainActivity.class);
                    startActivity(intent);*/
                }


            }
        });

        actionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallModel callModel = new CallModel();
                callModel.set_userInfo(u_name);
                callModel.set_roomId(room_name);
                callModel.set_fcmToken(fcm);
                callModel.set_status(value_send);
                callModel.set_type("decline");
                HttpHandler httpHandler = HttpHandler.getInstance();
                ServerResultHandler serverResultHandler = new ServerResultHandler(VideoActivity.this);
                httpHandler.set_resultHandler(serverResultHandler);
                httpHandler.placeCall(callModel, VideoActivity.this, APPConstant.SEND_FCM_NOTIFICATION_OPERATION);
                finish();
            }
        });
    }

    public void connectToRoom(String roomName) {
        configureAudio(true);
        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(accessToken).roomName(roomName);
        audioCodec = getAudioCodecPreference(APPConstant.PREF_AUDIO_CODEC, APPConstant.PREF_AUDIO_CODEC_DEFAULT);
        videoCodec = getVideoCodecPreference(APPConstant.PREF_VIDEO_CODEC, APPConstant.PREF_VIDEO_CODEC_DEFAULT);
        /*
         * Add local audio track to connect options to share with participants.
         */
        if (localAudioTrack != null) {
            connectOptionsBuilder
                    .audioTracks(Collections.singletonList(localAudioTrack));

        }
        System.out.println("VideoActivity| connectToRoom | localAudioTrack:" + localAudioTrack);

        /*
         * Add local video track to connect options to share with participants.
         */
        if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));

        }
        System.out.println("VideoActivity| connectToRoom | localVideoTrack:" + localVideoTrack);

        /*
         * Set the preferred audio and video codec for media.
         */
        connectOptionsBuilder.preferAudioCodecs(Collections.singletonList(audioCodec));
        connectOptionsBuilder.preferVideoCodecs(Collections.singletonList(videoCodec));

        /*
         * Set the sender side encoding parameters.
         */
        connectOptionsBuilder.encodingParameters(encodingParameters);
        System.out.println("VideoActivity | connect connectOptionsBuilder:" + connectOptionsBuilder);
//        System.out.println("VideoActivity | connect connectOptionsBuilder.build():" + connectOptionsBuilder.build());

        System.out.println("VideoActivity | connect roomListener:" + roomListener());
        System.out.println("VideoActivity | this" + VideoActivity.this);
        room = Video.connect(this, connectOptionsBuilder.build(), roomListener());
        //setDisconnectAction();
    }

    private AudioCodec getAudioCodecPreference(String key, String defaultValue) {
        final String audioCodecName = "OpusCodec.NAME";

        switch (audioCodecName) {
            case IsacCodec.NAME:
                return new IsacCodec();
            case OpusCodec.NAME:
                return new OpusCodec();
            case PcmaCodec.NAME:
                return new PcmaCodec();
            case PcmuCodec.NAME:
                return new PcmuCodec();
            case G722Codec.NAME:
                return new G722Codec();
            default:
                return new OpusCodec();
        }
//        return new OpusCodec();

    }

    private VideoCodec getVideoCodecPreference(String key, String defaultValue) {
        final String videoCodecName = "Vp8Codec.NAME";

        switch (videoCodecName) {
            case Vp8Codec.NAME:
                boolean simulcast = preferences.getBoolean(APPConstant.PREF_VP8_SIMULCAST,
                        APPConstant.PREF_VP8_SIMULCAST_DEFAULT);
                return new Vp8Codec(simulcast);
            case H264Codec.NAME:
                return new H264Codec();
            case Vp9Codec.NAME:
                return new Vp9Codec();
            default:
                return new Vp8Codec();
        }
//        return new Vp8Codec();
    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(Room room) {
                localParticipant = room.getLocalParticipant();
                videoStatusTextView.setText("Connected to " + room.getName());
                setTitle(room.getName());

                for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                    addRemoteParticipant(remoteParticipant);
                    break;
                }
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                videoStatusTextView.setText("Failed to connect");
                configureAudio(false);
                intializeUI();
                System.out.println("VideoActivity| connectToRoom | onConnectFailure:" + e);

            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
                localParticipant = null;
                videoStatusTextView.setText("Disconnected from " + room.getName());
                VideoActivity.this.room = null;
                // Only reinitialize the UI if disconnect was not called from onDestroy()
                if (!disconnectedFromOnDestroy) {
                    configureAudio(false);
                    intializeUI();
                    moveLocalVideoToPrimaryView();
                }
            }

            @Override
            public void onParticipantConnected(Room room, RemoteParticipant remoteParticipant) {
                addRemoteParticipant(remoteParticipant);

            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant remoteParticipant) {
                removeRemoteParticipant(remoteParticipant);
            }

            @Override
            public void onRecordingStarted(Room room) {
                /*
                 * Indicates when media shared to a Room is being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */

            }

            @Override
            public void onRecordingStopped(Room room) {
                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */

            }
        };
    }

    private void addRemoteParticipant(RemoteParticipant remoteParticipant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
            Snackbar.make(connectActionFab,
                    "Multiple participants are not currently support in this UI",
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        remoteParticipantIdentity = remoteParticipant.getIdentity();
        videoStatusTextView.setText("RemoteParticipant " + remoteParticipantIdentity + " joined");

        /*
         * Add remote participant renderer
         */
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Only render video tracks that are subscribed to
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                addRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }

        /*
         * Start listening for participant events
         */
        remoteParticipant.setListener(remoteParticipantListener());
    }

    private void addRemoteParticipantVideo(VideoTrack videoTrack) {
        moveLocalVideoToThumbnailView();
        primaryVideoView.setMirror(false);
        videoTrack.addRenderer(primaryVideoView);
    }

    private void moveLocalVideoToThumbnailView() {
        if (thumbnailVideoView.getVisibility() == View.GONE) {
            thumbnailVideoView.setVisibility(View.VISIBLE);
            localVideoTrack.removeRenderer(primaryVideoView);
            localVideoTrack.addRenderer(thumbnailVideoView);
            localVideoView = thumbnailVideoView;
            thumbnailVideoView.setMirror(cameraCapturerCompat.getCameraSource() == CameraCapturer.CameraSource.FRONT_CAMERA);
        }
    }

    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {

                videoStatusTextView.setText("onAudioTrackPublished");
            }

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {

                videoStatusTextView.setText("onAudioTrackUnpublished");
            }

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {

                videoStatusTextView.setText("onDataTrackPublished");
            }

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {

                videoStatusTextView.setText("onDataTrackUnpublished");
            }

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {

                videoStatusTextView.setText("onVideoTrackPublished");
            }

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {

                videoStatusTextView.setText("onVideoTrackUnpublished");
            }

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {

                videoStatusTextView.setText("onAudioTrackSubscribed");
            }

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {

                videoStatusTextView.setText("onAudioTrackUnsubscribed");
            }

            @Override
            public void onAudioTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                       TwilioException twilioException) {

                videoStatusTextView.setText("onAudioTrackSubscriptionFailed");
            }

            @Override
            public void onDataTrackSubscribed(RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              RemoteDataTrack remoteDataTrack) {

                videoStatusTextView.setText("onDataTrackSubscribed");
            }

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {

                videoStatusTextView.setText("onDataTrackUnsubscribed");
            }

            @Override
            public void onDataTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                      RemoteDataTrackPublication remoteDataTrackPublication,
                                                      TwilioException twilioException) {

                videoStatusTextView.setText("onDataTrackSubscriptionFailed");
            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {

                videoStatusTextView.setText("onVideoTrackSubscribed");
                addRemoteParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {

                videoStatusTextView.setText("onVideoTrackUnsubscribed");
                removeParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                       TwilioException twilioException) {

                videoStatusTextView.setText("onVideoTrackSubscriptionFailed");
                Snackbar.make(connectActionFab,
                        String.format("Failed to subscribe to %s video track",
                                remoteParticipant.getIdentity()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }
        };
    }

    private void removeParticipantVideo(VideoTrack videoTrack) {
        videoTrack.removeRenderer(primaryVideoView);
    }

    private void removeRemoteParticipant(RemoteParticipant remoteParticipant) {
        videoStatusTextView.setText("RemoteParticipant " + remoteParticipant.getIdentity() +
                " left.");
        if (!remoteParticipant.getIdentity().equals(remoteParticipantIdentity)) {
            return;
        }

        /*
         * Remove remote participant renderer
         */
        if (!remoteParticipant.getRemoteVideoTracks().isEmpty()) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Remove video only if subscribed to participant track
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                removeParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }
        moveLocalVideoToPrimaryView();
    }

    private void moveLocalVideoToPrimaryView() {
        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
            thumbnailVideoView.setVisibility(View.GONE);
            if (localVideoTrack != null) {
                localVideoTrack.removeRenderer(thumbnailVideoView);
                localVideoTrack.addRenderer(primaryVideoView);
            }
            localVideoView = primaryVideoView;
            primaryVideoView.setMirror(cameraCapturerCompat.getCameraSource() == CameraCapturer.CameraSource.FRONT_CAMERA);
        }
    }

    private void configureAudio(boolean enable) {
        if (enable) {
            previousAudioMode = audioManager.getMode();
            System.out.println("VideoActivity | configureAudio |audioManager.getMode():" + audioManager.getMode());
            // Request audio focus before making any device switch
            requestAudioFocus();
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when playout and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            /*
             * Always disable microphone mute during a WebRTC call.
             */
            previousMicrophoneMute = audioManager.isMicrophoneMute();
            audioManager.setMicrophoneMute(false);
        } else {
            audioManager.setMode(previousAudioMode);
            audioManager.abandonAudioFocus(null);
            audioManager.setMicrophoneMute(previousMicrophoneMute);
        }
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            AudioFocusRequest focusRequest =
                    new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(
                                    i -> {
                                    })
                            .build();
            audioManager.requestAudioFocus(focusRequest);
        } else {
            audioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }

    }

    private class ServerResultHandler implements iHttpResultHandler {
        private Context _context;


        public ServerResultHandler(Context context) {
            this._context = context;
        }

        @Override
        public void onSuccess(Object response, String operation_flag) {

            if (operation_flag.equalsIgnoreCase(APPConstant.SEND_FCM_NOTIFICATION_OPERATION)) {

            }

        }

        @Override
        public void onToken(TokenDataModel tokenDataModel, String operation_flag) {

        }

        @Override
        public void onCancel(Object response, String operation_flag) {

        }

        @Override
        public void onError(Object response, String operation_flag) {

        }

        @Override
        public void inProgress(Object response, String operation_flag) {

        }
    }

    private void createAudioAndVideoTracks() {
        // Share your microphone
        localAudioTrack = LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME);
        System.out.println("VideoActivity | createAudioAndVideoTracks |LocalAudioTrack :" + localAudioTrack);

        // Share your camera
        cameraCapturerCompat = new CameraCapturerCompat(this, getAvailableCameraSource());
        localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturerCompat.getVideoCapturer(), LOCAL_VIDEO_TRACK_NAME);
        primaryVideoView.setMirror(true);
        localVideoTrack.addRenderer(primaryVideoView);
        localVideoView = primaryVideoView;
        System.out.println("VideoActivity | createAudioAndVideoTracks |localVideoTrack :" + localVideoTrack);
    }

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this,
                    "permissions_needed",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CAMERA_MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private CameraCapturer.CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) ?
                (CameraCapturer.CameraSource.FRONT_CAMERA) :
                (CameraCapturer.CameraSource.BACK_CAMERA);
    }

    @Override
    protected void onResume() {
        System.out.println("VideoActivity | onResume | Resume Started");
        super.onResume();

        /*
         * Update preferred audio and video codec in case changed in settings
         */
        audioCodec = getAudioCodecPreference(APPConstant.PREF_AUDIO_CODEC,
                APPConstant.PREF_AUDIO_CODEC_DEFAULT);
        videoCodec = getVideoCodecPreference(APPConstant.PREF_VIDEO_CODEC,
                APPConstant.PREF_VIDEO_CODEC_DEFAULT);

        /*
         * Get latest encoding parameters
         */
        final EncodingParameters newEncodingParameters = getEncodingParameters();

        /*
         * If the local video track was released when the app was put in the background, recreate.
         */
        if (localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {
            localVideoTrack = LocalVideoTrack.create(this,
                    true,
                    cameraCapturerCompat.getVideoCapturer(),
                    LOCAL_VIDEO_TRACK_NAME);
            localVideoTrack.addRenderer(localVideoView);

            /*
             * If connected to a Room then share the local video track.
             */
            if (localParticipant != null) {
                localParticipant.publishTrack(localVideoTrack);

                /*
                 * Update encoding parameters if they have changed.
                 */
                if (!newEncodingParameters.equals(encodingParameters)) {
                    localParticipant.setEncodingParameters(newEncodingParameters);
                }
            }
        }

        /*
         * Update encoding parameters
         */
        encodingParameters = newEncodingParameters;
    }

    private EncodingParameters getEncodingParameters() {
        final int maxAudioBitrate = Integer.parseInt(
                preferences.getString(APPConstant.PREF_SENDER_MAX_AUDIO_BITRATE,
                        APPConstant.PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT));
        final int maxVideoBitrate = Integer.parseInt(
                preferences.getString(APPConstant.PREF_SENDER_MAX_VIDEO_BITRATE,
                        APPConstant.PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT));

        return new EncodingParameters(maxAudioBitrate, maxVideoBitrate);
    }

    @Override
    protected void onDestroy() {
        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }

        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            boolean cameraAndMicPermissionGranted = true;

            for (int grantResult : grantResults) {
                cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if (cameraAndMicPermissionGranted) {
                createAudioAndVideoTracks();

            } else {
                Toast.makeText(this,
                        R.string.permissions_needed,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
