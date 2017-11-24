/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobapptut.com.camera2videoimage.oo.test;

/**
 *
 * @author alulab14
 */
public class Previewer {
//
//    CameraDevice mCameraDevice;
//    ImageReader mImageReader;
//    CaptureRequestBuilder mCaptureRequestBuilder;
//    PreviewCaptureSession mPreviewCaptureSession;
//
//    int mCaptureState = STATE_PREVIEW;
//
//    public PreviewCaptureSession getmPreviewCaptureSession() {
//        return mPreviewCaptureSession;
//    }
//
//    private void lockFocus(Object mBackgroundHandler) {
//        mCaptureState = STATE_WAIT_LOCK;
//        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
//        try {
//            if (mIsRecording) {
//                mRecordCaptureSession.capture(mCaptureRequestBuilder.build(), mRecordCaptureCallback, mBackgroundHandler);
//            } else {
//                mPreviewCaptureSession.capture(mCaptureRequestBuilder.build(), mPreviewCaptureCallback, mBackgroundHandler);
//            }
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    void startPreview(Size mPreviewSize, Object mTextureView, Object mBackgroundHandler) {
//        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
//        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
//        Surface previewSurface = new Surface(surfaceTexture);
//
//        try {
//            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            mCaptureRequestBuilder.addTarget(previewSurface);
//
//            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()),
//                    new CameraCaptureSession.StateCallback() {
//                        @Override
//                        public void onConfigured(CameraCaptureSession session) {
//                            mPreviewCaptureSession = session;
//                            try {
//                                mPreviewCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(),
//                                        null, mBackgroundHandler);
//                            } catch (CameraAccessException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onConfigureFailed(CameraCaptureSession session) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Unable to setup camera preview", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }, null);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
}
