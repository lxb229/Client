/*******************************************************************************\
** GCloudVoice:GVoice.h Objective-C API for GCloudVoice
** Created by CZ  on 23/05/2017
**
**  Copyright © 2017 gcloud. All rights reserved.
\*******************************************************************************/

#ifndef GCloudVoice_GVoice_h_
#define GCloudVoice_GVoice_h_

#ifdef __APPLE__
#import <Foundation/Foundation.h>

enum GCloudVoiceErrno
{
    GCLOUD_VOICE_SUCC           = 0,
    
    //common base err
    GCLOUD_VOICE_PARAM_NULL = 0x1001,	//4097, some param is null
    GCLOUD_VOICE_NEED_SETAPPINFO = 0x1002,	//4098, you should call SetAppInfo first before call other api
    GCLOUD_VOICE_INIT_ERR = 0x1003,	//4099, Init Erro
    GCLOUD_VOICE_RECORDING_ERR = 0x1004,		//4100, now is recording, can't do other operator
    GCLOUD_VOICE_POLL_BUFF_ERR = 0x1005,	//4101, poll buffer is not enough or null
    GCLOUD_VOICE_MODE_STATE_ERR = 0x1006,	//4102, call some api, but the mode is not correct, maybe you shoud call SetMode first and correct
    GCLOUD_VOICE_PARAM_INVALID = 0x1007,	//4103, some param is null or value is invalid for our request, used right param and make sure is value range is correct by our comment
    GCLOUD_VOICE_OPENFILE_ERR = 0x1008, //4104, open a file err
    GCLOUD_VOICE_NEED_INIT = 0x1009, //4105, you should call Init before do this operator
    GCLOUD_VOICE_ENGINE_ERR = 0x100A, //4106, you have not get engine instance, this common in use c# api, but not get gcloudvoice instance first
    GCLOUD_VOICE_POLL_MSG_PARSE_ERR = 0x100B, //4107, this common in c# api, parse poll msg err
    GCLOUD_VOICE_POLL_MSG_NO = 0x100C, //4108, poll, no msg to update
    
    
    //realtime err
    GCLOUD_VOICE_REALTIME_STATE_ERR = 0x2001, //8193, call some realtime api, but state err, such as OpenMic but you have not Join Room first
    GCLOUD_VOICE_JOIN_ERR = 0x2002, //8194, join room failed
    GCLOUD_VOICE_QUIT_ROOMNAME_ERR = 0x2003,	//8195, quit room err, the quit roomname not equal join roomname
    GCLOUD_VOICE_OPENMIC_NOTANCHOR_ERR = 0x2004,//8196, open mic in bigroom,but not anchor role
    
    
    //message err
    GCLOUD_VOICE_AUTHKEY_ERR = 0x3001, //12289, apply authkey api error
    GCLOUD_VOICE_PATH_ACCESS_ERR = 0x3002, //12290, the path can not access ,may be path file not exists or deny to access
    GCLOUD_VOICE_PERMISSION_MIC_ERR = 0x3003,	//12291, you have not right to access micphone in android
    GCLOUD_VOICE_NEED_AUTHKEY = 0x3004,		//12292,you have not get authkey, call ApplyMessageKey first
    GCLOUD_VOICE_UPLOAD_ERR = 0x3005,	//12293, upload file err
    GCLOUD_VOICE_HTTP_BUSY = 0x3006,	//12294, http is busy,maybe the last upload/download not finish.
    GCLOUD_VOICE_DOWNLOAD_ERR = 0x3007,	//12295, download file err
    GCLOUD_VOICE_SPEAKER_ERR = 0x3008, //12296, open or close speaker tve error
    GCLOUD_VOICE_TVE_PLAYSOUND_ERR = 0x3009, //12297, tve play file error
    GCLOUD_VOICE_AUTHING = 0x300a, // 12298, Already in applying auth key processing
    
    GCLOUD_VOICE_INTERNAL_TVE_ERR = 0x5001,		//20481, internal TVE err, our used
    GCLOUD_VOICE_INTERNAL_VISIT_ERR = 0x5002,	//20482, internal Not TVE err, out used
    GCLOUD_VOICE_INTERNAL_USED = 0x5003, //20483, internal used, you should not get this err num
    
    GCLOUD_VOICE_BADSERVER = 0x06001, // 24577, bad server address,should be "udp://capi.xxx.xxx.com"
    
    GCLOUD_VOICE_STTING =  0x07001, // 28673, Already in speach to text processing
    
};

enum GCloudVoiceCompleteCode
{
    GV_ON_JOINROOM_SUCC = 1,	//join room succ
    GV_ON_JOINROOM_TIMEOUT,  //join room timeout
    GV_ON_JOINROOM_SVR_ERR,  //communication with svr occur some err, such as err data recv from svr
    GV_ON_JOINROOM_UNKNOWN, //reserved, our internal unknow err
    
    GV_ON_NET_ERR,  //net err,may be can't connect to network
    
    GV_ON_QUITROOM_SUCC, //quitroom succ, if you have join room succ first, quit room will alway return succ
    
    GV_ON_MESSAGE_KEY_APPLIED_SUCC,  //apply message authkey succ
    GV_ON_MESSAGE_KEY_APPLIED_TIMEOUT,		//apply message authkey timeout
    GV_ON_MESSAGE_KEY_APPLIED_SVR_ERR,  //communication with svr occur some err, such as err data recv from svr
    GV_ON_MESSAGE_KEY_APPLIED_UNKNOWN,  //reserved,  our internal unknow err
    
    GV_ON_UPLOAD_RECORD_DONE,  //upload record file succ
    GV_ON_UPLOAD_RECORD_ERROR,  //upload record file occur error
    GV_ON_DOWNLOAD_RECORD_DONE,	//download record file succ
    GV_ON_DOWNLOAD_RECORD_ERROR,	//download record file occur error
    
    GV_ON_STT_SUCC, // speech to text successful
    GV_ON_STT_TIMEOUT, // speech to text with timeout
    GV_ON_STT_APIERR, // server's error
    
    GV_ON_RSTT_SUCC, // speech to text successful
    GV_ON_RSTT_TIMEOUT, // speech to text with timeout
    GV_ON_RSTT_APIERR, // server's error
    
    GV_ON_PLAYFILE_DONE,  //the record file played end
    
    GV_ON_ROOM_OFFLINE, // Dropped from the room
    GV_ON_UNKNOWN,
};

/**
 * Destination language to translate to.
 */

enum GCloudLanguage {
    China       = 0,
    Korean      = 1,
    English     = 2,
    Japanese    = 3,
};

/**
 * Mode of voice engine.
 *
 * You should set to one first.
 */
enum GCloudVoiceMode
{
    RealTime = 0, // realtime mode for TeamRoom or NationalRoom
    Messages,     // voice message mode
    Translation,  // speach to text mode
    RSTT, // real-time speach to text mode
    HIGHQUALITY = 4,
};

/**
 * Member's role for National Room.
 */
enum GCloudVoiceMemberRole
{
    Anchor = 1, // member who can open microphone and say
    Audience,   // member who can only hear anchor's voice
};
//Voice Effects Mode
enum SoundEffects
{
	GVSE_REVB_CHURCH = 0,
	GVSE_REVB_THEATER = 1,
	GVSE_HELL = 2,
	GVSE_ROBOT_1 = 3,
	GVSE_MALE_TO_FEMALE = 4,
	GVSE_FEMALE_TO_MALE = 5,
	GVSE_DRUNK = 6,
	GVSE_PAPI_JIANG = 7,
	GVSE_SQUIRREL = 8,
	GVSE_NO_EFFECT = 9
};

@protocol GVGCloudVoiceDelegate <NSObject>


/**
 * Callback when JoinXxxRoom successful or failed.
 *
 * @param code : a GCloudVoiceCompleteCode code . You should check this first.
 * @param roomName : name of your joining, should be 0-9A-Za-Z._- and less than 127 bytes
 * @param memberID : if success, return the memberID
 */
- (void) onJoinRoom:(enum GCloudVoiceCompleteCode) code withRoomName: (const char * _Nullable)roomName andMemberID:(int) memberID;

/**
 * Callback when dropped from the room
 *
 * @param code : a GCloudVoiceCompleteCode code . You should check this first.
 * @param roomName : name of your joining, should be 0-9A-Za-Z._- and less than 127 bytes
 * @param memberID : if success, return the memberID
 */
- (void) onStatusUpdate:(enum GCloudVoiceCompleteCode) status withRoomName: (const char * _Nullable)roomName andMemberID:(int) memberID;

/**
 * Callback when QuitRoom successful or failed.
 *
 * @param code : a GCloudVoiceCompleteCode code . You should check this first.
 * @param roomName : name of your joining, should be 0-9A-Za-Z._- and less than 127 bytes
 */
- (void) onQuitRoom:(enum GCloudVoiceCompleteCode) code withRoomName: (const char * _Nullable)roomName;

/**
 * Callback when someone saied or silence in the same room.
 *
 * @param count: count of members who's status has changed.
 * @param members: a int array composed of [memberid_0, status,memberid_1, status ... memberid_2*count, status]
 * here, status could be 0, 1, 2. 0 meets silence and 1/2 means saying
 */
- (void) onMemberVoice:	(const unsigned int * _Nullable)members withCount: (int) count;

// Voice Message Callback
/**
 * Callback when upload voice file successful or failed.
 *
 * @param code: a GCloudVoiceCompleteCode code . You should check this first.
 * @param filePath: file to upload
 * @param fileID: if success ,get back the id for the file.
 */
- (void) onUploadFile: (enum GCloudVoiceCompleteCode) code withFilePath: (const char * _Nullable)filePath andFileID:(const char * _Nullable)fileID ;

/**
 * Callback when download voice file successful or failed.
 *
 * @param code: a GCloudVoiceCompleteCode code . You should check this first.
 * @param filePath: file to download to .
 * @param fileID: if success ,get back the id for the file.
 */
- (void) onDownloadFile: (enum GCloudVoiceCompleteCode) code  withFilePath: (const char * _Nullable)filePath andFileID:(const char * _Nullable)fileID;

/**
 * Callback when finish a voice file play end.
 *
 * @param code: a GCloudVoiceCompleteCode code . You should check this first.
 * @param filePath: file had been plaied.
 */
- (void) onPlayRecordedFile:(enum GCloudVoiceCompleteCode) code withFilePath: (const char * _Nullable)filePath;

/**
 * Callback when query message key successful or failed.
 *
 * @param code: a GCloudVoiceCompleteCode code . You should check this first.
 */
- (void) onApplyMessageKey:(enum GCloudVoiceCompleteCode) code;

// Translate
/**
 * Callback when translate voice to text successful or failed.
 *
 * @param code: a GCloudVoiceCompleteCode code . You should check this first.
 * @param fileID : file to translate
 * @param result : the destination text of the destination language.
 */
- (void) onSpeechToText:(enum GCloudVoiceCompleteCode) code withFileID:(const char * _Nullable)fileID andResult:( const char * _Nullable)result;

/**
 * Callback when client is using microphone recording audio
 *
 * @param pAudioData : audio data pointer
 * @param nDataLength : audio data length
 * @param result : void
 */
- (void) onRecording:(const unsigned char* _Nullable) pAudioData withLength: (unsigned int) nDataLength;


/**
 * Callback when translate voice to text successful or failed.
 */
- (void) onStreamSpeechToText:(enum GCloudVoiceCompleteCode) code withError:(int) error andResult:(const char *_Nullable)result;

@end

@interface GVGCloudVoice : NSObject
+ (GVGCloudVoice* _Nullable) sharedInstance;

@property (nonatomic, weak, nullable) id <GVGCloudVoiceDelegate> delegate;


/**
 * Set the server address, just used for foreign game,such as Korea, Europe...
 *
 * @param URL: url of server
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) setServerInfo:(const char * _Nullable) URL;

/**
 * Set your app's info such as appID/appKey.
 *
 * @param appID : your game ID from gcloud.qq.com
 * @param appKey : your game key from gcloud.qq.com
 * @param openID : player's openID from QQ or Wechat. or a unit role ID.
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) setAppInfo:(const char *_Nullable) appID withKey: (const char *_Nullable)appKey andOpenID:(const char *_Nullable)openID;

/**
 * Init the voice engine.
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) initEngine;

/**
 * Set the mode for engine.
 *
 * @param mode: mode to set
 *              RealTime:    realtime mode for TeamRoom or NationalRoom
 *              Messages:    voice message mode
 *              Translation: speach to text mode
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) setMode:(enum GCloudVoiceMode) mode;

/**
 * Trigger engine's callback.
 *
 * You should invoke poll on your loop. such as onUpdate() in cocos2dx
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) poll;

/**
 * The Application's Pause.
 *
 * When your app pause such as goto backend you should invoke this
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) pause;

/**
 * The Application's Resume.
 *
 * When your app reuse such as come back from  backend you should invoke this
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) resume;


#pragma mark Real-Time Voice API
/**
 * Join in team room.
 *
 * @param roomName: the room to join, should be less than 127byte, composed by alpha.
 * @param msTimeout: time for join, it is micro second. value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) joinTeamRoom:(const char * _Nullable)roomName timeout: (int) msTimeout;



/**
 * Join in a national room.
 *
 * @param roomName: the room to join, should be less than 127byte, composed by alpha.
 * @param role : a GCloudVoiceMemberRole value illustrate wheather can send voice data.
 * @param msTimeout: time for join, it is micro second. value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) joinNationalRoom:(const char *_Nullable)roomName role: (enum GCloudVoiceMemberRole) role timeout: (int) msTimeout;


/**
 * Join in a FM room.
 *
 * @param roomName: the room to join, should be less than 127byte, composed by alpha.
 * @param msTimeout: time for join, it is micro second. value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) joinFMRoom:(const char *_Nullable)roomName timeout:(int) msTimeout;

/**
 * Quit the voice room.
 *
 * @param roomName: the room to join, should be less than 127byte, composed by alpha.
 * @param msTimeout: time for quit, it is micro second.value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) quitRoom:(const char * _Nullable)roomName timeout:(int) msTimeout ;

/**
 * Open player's micro phone  and begin to send player's voice data.
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) openMic;

/**
 * Close players's micro phone and stop to send player's voice data.
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) closeMic;

/**
 * Open player's speaker and begin recvie voice data from the net .
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) openSpeaker;

/**
 * Close player's speaker and stop to recive voice data from the net.
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) closeSpeaker;


#pragma mark  Messages Voice API
/**
 * Apply the key for voice message.
 *
 * @param msTimeout: time for apply, it is micro second.value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) applyMessageKey:(int) msTimeout;

/**
 * Limit the max voice message's last time.
 *
 * @param msTime : message's largest time in micro second.value range[1000, 2*60*1000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) setMaxMessageLength:(int) msTime;

/**
 * Open player's microphone and record player's voice.
 *
 * @param filePath: voice data to store. file path should be such as:"your_dir/your_file_name" be sure to use "/" not "\"
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) startRecording:(const char *_Nullable) filePath;


/**
 * Open player's microphone and record player's voice.
 *
 * @param filePath: voice data to store. file path should be such as:"your_dir/your_file_name" be sure to use "/" not "\"
 * @param bOptim : true flag means recording audio not enter voip mode
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) startRecording:(const char *_Nullable) filePath bOptim : (BOOL) bOptim;

/**
 * Stop player's microphone and stop record player's voice.
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) stopRecording;

/**
 * Upload player's voice message file to the net.
 *
 * @param filePath: voice data to upload. file path should be such as "your_dir/your_file_name" be sure to use "/" not "\"
 * @param msTimeout: time for upload, it is micro second.value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) uploadRecordedFile:(const char *_Nullable) filePath timeout: (int) msTimeout ;

/**
 * Download other players' voice message.
 *
 * @param fileID : file to be download
 * @param filePath: voice data to store. file path should be such as "your_dir/your_file_name" be sure to use "/" not "\"
 * @param msTimeout: time for download, it is micro second.value range[5000, 60000]
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) downloadRecordedFile:(const char *_Nullable)fileID filePath:(const char *_Nullable) downloadFilePath timeout: (int) msTimeout ;

/**
 * Play local voice message file.
 *
 * @param filePath: voice data to play. file path should be such as:"your_dir/your_file_name" be sure to use "/" not "\"
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) playRecordedFile:(const char *_Nullable) downloadFilePath;

/**
 * Stop play the file.
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) stopPlayFile;


- (enum GCloudVoiceErrno)  enableSpeakerOn:(BOOL) bEnable;

/**
 * Translate voice data to text.
 *
 * @param fileID : file to be translate
 * @param msTimeout : timeout for translate
 * @param language: a GCloudLanguage indicate which language to be translate
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) speechToText:(const char *_Nullable)fileID timeout:(int) msTimeout language:(enum GCloudLanguage) language ;


#pragma mark if need
- (enum GCloudVoiceErrno) joinTeamRoom:(const char *_Nullable)roomName token: (const char *_Nullable)token timestamp:(int) timestamp timeout:(int) msTimeout;


- (enum GCloudVoiceErrno) joinNationalRoom:(const char *_Nullable)roomName role: ( enum GCloudVoiceMemberRole) role token: (const char * _Nullable)token timestamp: (int) timestamp timeout: (int) msTimeout ;

- (enum GCloudVoiceErrno) applyMessageKey:(const char *_Nullable)token timestamp:(int) timestamp timeout:(int) msTimeout;

- (enum GCloudVoiceErrno) speechToText:(const char *_Nullable)fileID token:(const char *_Nullable)token timestamp:(int) timestamp timeout:(int) msTimeout  language:(enum GCloudLanguage) language ;

- (enum GCloudVoiceErrno) startBlueTooth ;
- (enum GCloudVoiceErrno) stopBlueTooth ;

#pragma mark Extension

/**
 * Don't play voice of the member.
 *
 * @param member : member to forbid
 * @param bEnable : do forbid if it is false
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) forbidMemberVoice:(int) member enable:(BOOL) bEnable;

/**
 * Open Voice Engine's logcat
 *
 * @param enable: open logcat if it is true
 */
- (void) enableLog:(BOOL) enable;

/**
 * Get micphone's volume
 *
 * @return : micphone's volume , if return value>0, means you have said something capture by micphone
 */
- (int) getMicLevel:(BOOL) fadeOut;

/**
 * Get speaker's volume
 *
 * @return : speaker's volume, value is equal you Call SetSpeakerVolume param value
 */
- (int) getSpeakerLevel;

/**
 * set sepaker's volume
 *
 * @param vol: setspeakervolume,
 * Android & IOS, value range is 0-800, 100 means original voice volume, 50 means only 1/2 original voice volume, 200 means double original voice volume
 * Windows value range is 0x0-0xFFFF, suggested value bigger than 0xff00, then you can hear you speaker sound
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) setSpeakerVolume:(int) vol;

/**
 * Test wheather microphone is available
 *
 * @return : if success return GCLOUD_VOICE_SUCC, , means have detect micphone device,failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) testMic;

/**
 * Get voice message's info
 *
 * @param filepath: file path should be "sdcard/your_dir/your_file_name" be sure to use "/" not "\"
 * @param bytes: on return for file's size
 * @param sendons: on return for voice's length
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) getFileParam:(const char *_Nullable)filepath data:(unsigned int* _Nullable) bytes time:(float * _Nullable)seconds;

/**
 * Capture microphone audio data by IGCloudVoiceNotify::OnRecording
 *
 * @param bCapture : true/false - start/stop capturing audio data
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) captureMicrophoneData:(BOOL) bCapture;

/*
* control audio volume
* @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
- (BOOL) isSpeaking;

 * SetVoiceEffects
 *
 * @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
 * @see : GCloudVoiceErrno
 */
- (enum GCloudVoiceErrno) SetVoiceEffects:(enum SoundEffects) mode;

/*
* control audio volume
* @param : 100 means same as it has been recorded, 50 means half volume, 200 means double volume
* @return : if success return GCLOUD_VOICE_SUCC, failed return other errno @see GCloudVoiceErrno
*/
- (int) setMicVolume:(int)volume;

/**
 * YOU SHOULD NOT INVOKE THIS ONE.
 */
- (int) invoke: (unsigned int)    nCmd
          arg1: (unsigned int)    nParam1
          arg2: (unsigned int)    nParam2
          arg3: (unsigned int *_Nullable)pOutput;


@end

#endif /* __APPLE__ */
#endif /* GCloudVoice_GVoice_h_ */
